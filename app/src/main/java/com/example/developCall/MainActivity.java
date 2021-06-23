package com.example.developCall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.developCall.Object.AmazonTranscription;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

import jodd.http.HttpResponse;


public class MainActivity extends AppCompatActivity {


    Gson gson = new Gson();
    Button upload;
    TextView textView;



    AWSCredentials basicAWSCendentials;



    AmazonS3 s3;



    AmazonTranscribeClient client;
    StartTranscriptionJobRequest request;
    Media media = new Media();
    Settings settings = new Settings();






    String Accesskey;
    String Secretkey;
    String transcriptionJobName = "myJob2";
    GetTranscriptionJobRequest jobRequest;
    TranscriptionJob transcriptionJob;

    AmazonTranscription amazonTranscription = new AmazonTranscription();


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
        textView = findViewById(R.id.textView);




        Accesskey = BuildConfig.ACCESSKEY;
        Secretkey = BuildConfig.SECRETKEY;
        basicAWSCendentials = new BasicAWSCredentials(Accesskey, Secretkey);


        s3 = new AmazonS3Client(basicAWSCendentials);
        s3.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
        s3.setEndpoint("s3.ap-northeast-2.awszonaws.com");


        client = new AmazonTranscribeClient(basicAWSCendentials);






        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        request = new StartTranscriptionJobRequest();
                        request.withLanguageCode(LanguageCode.KoKR);
                        media.setMediaFileUri("s3://developcallab75aa449e42449d9139d48c077cd9a8104304-staging/통화 녹음 김현국_210623_143556.m4a");
                        settings.setShowSpeakerLabels(true);
                        settings.setMaxSpeakerLabels(2);

                        //내 버킷에서 미디어르 가져오는
                        request.withMedia(media).withMediaSampleRateHertz(44100);
                        request.setTranscriptionJobName(transcriptionJobName);
                        request.withMediaFormat("mp4");
                        request.withSettings(settings);
                        client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_2));
                        client.startTranscriptionJob(request);
                        //이게 시작을 해야 transcription을 작성할 수 있다.


                        jobRequest = new GetTranscriptionJobRequest();
                        jobRequest.setTranscriptionJobName(transcriptionJobName);


                        while (true)
                        {
                                            transcriptionJob = client.getTranscriptionJob(jobRequest).getTranscriptionJob();
                                            if (transcriptionJob.getTranscriptionJobStatus().equals(TranscriptionJobStatus.COMPLETED.name())) {

                                                try {
                                                    amazonTranscription = download(transcriptionJob.getTranscript().getTranscriptFileUri());
                                                    Log.d("result", amazonTranscription.getResults().getTranscripts().toString());

                                                    item1 = amazonTranscription.getResults().getSpeaker_labels().toString();
                                                    item2 = amazonTranscription.getResults().getItems().toString();


                                                    item1 = item1.replace("[","").replace("]","");
                                                    item2= item2.replace("[","").replace("]","");


                                                    dummy1 = item1.split(",");
                                                    array1 = new String[dummy1.length][];
                                                    int r=0;
                                                    for(String row : dummy1)
                                                    {
                                                        array1[r++] = row.split("/",5);


                                                    }

                                                    Log.d("item2", item2);
                                                    dummy2= item2.split(",");
                                                    array2 = new String[dummy2.length][];
                                                    int k=0;
                                                    for(String row1: dummy2)
                                                    {
                                                        array2[k++] = row1.split("/",5);

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


                        for(int i=0; i< array1.length; i++)
                        {
                            for(int j =0; j<array2.length;j++ )
                            {
                                if(array1[i][0].equals( array2[j][0]))
                                {


                                    array1[i][3]= array2[j][3];

                                }
                            }
                        }
                        System.out.println(Arrays.deepToString(array1));
                        Intent in = new Intent(getApplicationContext(),Chat.class);
                        in.putExtra("chatArray", array1);
                        startActivity(in);










                    }
                });

            }
        });


    }


    private AmazonTranscription download(String uri) throws IOException {


        jodd.http.HttpRequest httpRequest = jodd.http.HttpRequest.get(uri);
        HttpResponse response = httpRequest.get(uri).send();

        String result = response.charset("UTF-8").bodyText();
        Log.d("텍스트", result);






       return gson.fromJson(result , AmazonTranscription.class);
    }
}