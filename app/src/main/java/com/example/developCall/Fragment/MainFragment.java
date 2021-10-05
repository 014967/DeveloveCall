package com.example.developCall.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.amplifyframework.core.Amplify;
import com.example.developCall.ChatActivity;
import com.example.developCall.ContactActivity;
import com.example.developCall.Function.S3Upload;
import com.example.developCall.LoginActivity;
import com.example.developCall.Object.AmazonTranscription;
import com.example.developCall.R;
import com.example.developCall.BuildConfig;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

import jodd.http.HttpResponse;


public class MainFragment extends Fragment {


    Gson gson = new Gson();
    Button upload;
    Button btn_s3Upload;
    Button btn_logout;

    Button btn_addGroup;


    S3Upload s3Upload = new S3Upload();

    String filename = "";

    //transcribe
    AWSCredentials basicAWSCendentials;
    String Accesskey;
    String Secretkey;
    //String transcriptionJobName;
    GetTranscriptionJobRequest jobRequest;
    TranscriptionJob transcriptionJob;
    AmazonTranscription amazonTranscription = new AmazonTranscription();
    AmazonTranscribeClient client;
    StartTranscriptionJobRequest request;
    Media media = new Media();
    Settings settings = new Settings();


    //merge array
    String item1;
    String item2;
    String[] dummy1;
    String[][] array1;
    String[] dummy2;
    String[][] array2;


    String[][] modifyArray;

    String preSpk = "";
    String preContent = "";
    String postSpk = "";
    String postContent = "";
    int preIndex = 0;
    String preEndtime = "";
    String postEndtime = "";
    int changeCount = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_main, container, false);

        upload = view.findViewById(R.id.upload);
        btn_s3Upload = view.findViewById(R.id.s3Upload);
        btn_logout = view.findViewById(R.id.btn_logout);

        btn_addGroup = view.findViewById(R.id.btn_addGroup);


        Accesskey = BuildConfig.ACCESSKEY;
        Secretkey = BuildConfig.SECRETKEY;
        basicAWSCendentials = new BasicAWSCredentials(Accesskey, Secretkey);
        client = new AmazonTranscribeClient(basicAWSCendentials);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                Intent in = new Intent(requireActivity().getApplicationContext(), LoginActivity.class);
                requireActivity().startActivity(in);
                requireActivity().finish();

            }
        });

        btn_s3Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(v);
            }
        });


        btn_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent in = new Intent(getActivity(), ContactActivity.class);
                startActivity(in);
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        request = new StartTranscriptionJobRequest();
                        request.withLanguageCode(LanguageCode.KoKR);
                        // s3 버킷 주소

                        media.setMediaFileUri("s3://dev2734152e23e74f3d8dc72ea901c878c6144123-dev/public/" + filename);

                        settings.setShowSpeakerLabels(true);
                        settings.setMaxSpeakerLabels(2);

                        //내 버킷에서 미디어르 가져오는
                        request.withMedia(media).withMediaSampleRateHertz(44100);
                        request.setTranscriptionJobName(filename);
                        //request.withMediaFormat("mp4");
                        request.withSettings(settings);
                        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
                        client.startTranscriptionJob(request);
                        //이게 시작을 해야 transcription을 작성할 수 있다.


                        jobRequest = new GetTranscriptionJobRequest();
                        jobRequest.setTranscriptionJobName(filename);


                        while (true) {
                            transcriptionJob = client.getTranscriptionJob(jobRequest).getTranscriptionJob();
                            if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {

                                try {
                                    amazonTranscription = download(transcriptionJob.getTranscript().getTranscriptFileUri());
                                    Log.d("result", amazonTranscription.getResults().getTranscripts().toString());

                                    item1 = amazonTranscription.getResults().getSpeaker_labels().toString();
                                    item2 = amazonTranscription.getResults().getItems().toString();


                                    item1 = item1.replace("[", "").replace("]", "");
                                    item2 = item2.replace("[", "").replace("]", "");


                                    dummy1 = item1.split(",");
                                    array1 = new String[dummy1.length][];
                                    int r = 0;
                                    for (String row : dummy1) {
                                        array1[r++] = row.split("/", 5);


                                    }

                                    Log.d("item2", item2);
                                    dummy2 = item2.split(",");
                                    array2 = new String[dummy2.length][];
                                    int k = 0;
                                    for (String row1 : dummy2) {
                                        array2[k++] = row1.split("/", 5);

                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                break;

                            } else if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.FAILED.name())) {

                                break;
                            }
                            // to not be so anxious
                            synchronized (this) {
                                try {
                                    this.wait(50);
                                } catch (InterruptedException e) {
                                }
                            }

                        }


                        for (int i = 0; i < array1.length; i++) {
                            for (int j = 0; j < array2.length; j++) {
                                if (array1[i][0].equals(array2[j][0])) {


                                    array1[i][3] = array2[j][3];

                                }
                            }
                        }


                        for (int i = 0; i < array1.length; i++) {


                            if (preSpk.equals("")) {
                                preSpk = array1[i][2];
                                preContent = array1[i][3];
                                preIndex = i;
                                preEndtime = array1[i][1];
                            } else {
                                postSpk = array1[i][2];
                                postContent = array1[i][3];
                                postEndtime = array1[i][1];
                                if (preSpk.equals(postSpk)) {
                                    //뒤에 오는 spk와 같은 경우
                                    preContent = preContent + " " + postContent;
                                    preEndtime = postEndtime;
                                } else {
                                    //!preSpk.equals(array[i][2])
                                    array1[preIndex][3] = preContent;
                                    array1[preIndex][1] = postEndtime;

                                    preSpk = postSpk;
                                    preContent = postContent;
                                    preEndtime = postEndtime;
                                    preIndex = i;
                                    changeCount++;

                                }
                            }


                        }

                        preSpk = "";
                        modifyArray = new String[changeCount + 1][4];
                        int k = 0;
                        for (int i = 0; i < array1.length; i++) //array1 탐색
                        {

                            if (preSpk.equals(""))  //맨처음에
                            {

                                for (int j = 0; j < 4; j++) {
                                    modifyArray[k][j] = array1[i][j]; //modifyArray[0][0]에 array1[0]의 값는 넣는다.
                                }
                                k++;    //k = 1
                                preSpk = array1[i][2];  // prespk = array[0][2];
                            } else // i =2 부터 시작
                            {
                                postSpk = array1[i][2];  //array1[2][2];
                                if (!preSpk.equals(postSpk))  // preSpk = array[0][2]; 둘은 spk가 같아. 그래서 modifiy에 넣을 필요 x
                                {
                                    //spk가 달라진 순간에
                                    for (int m = 0; m < 4; m++) {
                                        modifyArray[k][m] = array1[i][m];
                                    }
                                    k++;
                                    preSpk = postSpk;

                                }
                            }


                        }


                        Intent in = new Intent(requireActivity().getApplicationContext(), ChatActivity.class);
                        in.putExtra("chatArray", modifyArray);
                        startActivity(in);
                    }
                });


            }
        });


        return view;

    }


    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == requestcode && resultcode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            filename = getFileName(uri);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                s3Upload.upload(filename, inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //String src = uri.getPath();


        }
    }

    int requestcode = 1;

    public void openFileChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestcode);
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private AmazonTranscription download(String uri) throws IOException {


        jodd.http.HttpRequest httpRequest = jodd.http.HttpRequest.get(uri);
        HttpResponse response = httpRequest.get(uri).send();

        String result = response.charset("UTF-8").bodyText();
        Log.d("텍스트", result);


        return gson.fromJson(result, AmazonTranscription.class);
    }

}