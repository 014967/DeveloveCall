package com.example.developCall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.transcribe.AmazonTranscribeClient;
import com.amazonaws.services.transcribe.model.GetTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.LanguageCode;
import com.amazonaws.services.transcribe.model.Media;
import com.amazonaws.services.transcribe.model.Settings;
import com.amazonaws.services.transcribe.model.StartTranscriptionJobRequest;
import com.amazonaws.services.transcribe.model.TranscriptionJob;
import com.amazonaws.services.transcribe.model.TranscriptionJobStatus;
import com.amplifyframework.core.Amplify;
import com.example.developCall.Function.S3Upload;
import com.example.developCall.Object.AmazonTranscription;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;

import jodd.http.HttpResponse;


public class MainActivity extends AppCompatActivity {


    Gson gson = new Gson();
    Button upload;
    Button btn_s3Upload;



    S3Upload s3Upload = new S3Upload();
    String filename;






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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        upload = findViewById(R.id.upload);
        btn_s3Upload = findViewById(R.id.s3Upload);





        Accesskey = BuildConfig.ACCESSKEY;
        Secretkey = BuildConfig.SECRETKEY;
        basicAWSCendentials = new BasicAWSCredentials(Accesskey, Secretkey);
        client = new AmazonTranscribeClient(basicAWSCendentials);



        btn_s3Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser(v);
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
                                media.setMediaFileUri("s3://developcallaudiofile212551-dev/public/" + filename);
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


                                System.out.println(Arrays.deepToString(array1));
                                System.out.println(Arrays.deepToString(array2));


                                for (int i = 0; i < array1.length; i++) {
                                    for (int j = 0; j < array2.length; j++) {
                                        if (array1[i][0].equals(array2[j][0])) {


                                            array1[i][3] = array2[j][3];

                                        }
                                    }
                                }
                                System.out.println(Arrays.deepToString(array1));
                                Intent in = new Intent(getApplicationContext(), Chat.class);
                                in.putExtra("chatArray", array1);
                                startActivity(in);


                            }
                        });

                    }
                });




    }

    int requestcode =1;
    @Override
    public void onActivityResult(int requestcode , int resultcode, Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode == requestcode && resultcode == Activity.RESULT_OK)
        {
            if(data==null) {
                return;
            }
            Uri uri = data.getData();
            filename = getFileName(uri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                s3Upload.upload(filename, inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //String src = uri.getPath();


        }
    }

    public void openFileChooser(View view)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, requestcode);
    }

    public String getFileName(Uri uri)
    {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
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






       return gson.fromJson(result , AmazonTranscription.class);
    }





}