console.log('Loading function');

const aws = require('aws-sdk');
const transcribe = new aws.TranscribeService(),
        path = require('path'),
        LANGUAGE_CODE = "ko-KR",
        OUTPUT_BUCKET = "developcall-transcribe-output";


const s3 = new aws.S3({ apiVersion: '2006-03-01' });


exports.handler =  (event, context) => {


    console.log(event);
    console.log(context);
    console.log(context);
    const eventRecord =  event.Records[0];
    const bucket = eventRecord.s3.bucket.name;
    //const key = decodeURIComponent(event.Records[0].s3.object.key.replace(/\+/g, ' '));
    const keyArray = eventRecord.s3.object.key.split('/');
    const key = keyArray[1];
    const id = context.awsRequestId;
    const mediaFormatArray = key.split('.');
    var mediaFormat = mediaFormatArray[1];
    console.log("mediaFormat : " , mediaFormat);

   // let extension = path.extname(key);
    //extension = extension.substr(1, extension.length);
    console.log('key from ' , key);
    console.log('lang from ' , LANGUAGE_CODE);
    console.log('output from ' , OUTPUT_BUCKET);


    if(mediaFormat=='m4a')
    {
        mediaFormat = 'mp4';
        console.log('m4a -> mp4');
    }

    if (!['mp3', 'mp4', 'wav', 'flac'].includes(mediaFormat)) {
     throw 'Invalid file extension, the only supported AWS Transcribe file types are MP3, MP4, WAV, FLAC.';
    }

  const fileUri = `https://${bucket}.s3.amazonaws.com/public/${key}`,
    jobName = `${key}`;

    const params = {
        LanguageCode : LANGUAGE_CODE,
        Media : {
            MediaFileUri : fileUri
        },
        MediaFormat : mediaFormat,
        TranscriptionJobName : jobName,
        OutputBucketName : OUTPUT_BUCKET,
        Settings : {
            MaxSpeakerLabels : 2,
            ShowSpeakerLabels : true,
        }
    };

    try{
        return transcribe.startTranscriptionJob(params).promise();
    }
    catch(err)
    {
        console.log(err);
        throw new Error(err);
    }


};
