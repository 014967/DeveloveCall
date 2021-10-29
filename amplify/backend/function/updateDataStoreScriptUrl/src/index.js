

var aws = require('aws-sdk');
var docClient = new aws.DynamoDB.DocumentClient();

const { randomUUID } = require('crypto');

const s3 = new aws.S3({ apiVersion: '2006-03-01' });
const lambda = new aws.Lambda();

Items = function(start_time, end_time, spk, content){
this.start_time = start_time;
this.end_time = end_time;
this.spk = spk;
this.content = content;

}







exports.handler = async (event , context) => {
    // TODO implement



console.log(randomUUID());



console.log(event)


// get id from event
    var jobName = event.detail.TranscriptionJobName;
    var removeFormatString = jobName.split(".");

    //var dirtyNumber =  /(.*)(-.+)(-.+)(?<=$)/.exec(removeFormatString[0]);
    var dirtyNumber = removeFormatString[0].split("_");
    var getScript ="";

    var uid = context.awsRequestId;
    var userId = dirtyNumber[0];
    var friendId = dirtyNumber[1];
    var uploadTime = dirtyNumber[2];
    var fileUrl = "s3://developcall-transcribe-output/" + jobName + ".json"

    var tablename = "Chat-6tai4sqoubaihizrtoyvo7a5da-dev";

    var friendTableName = "Friend-6tai4sqoubaihizrtoyvo7a5da-dev";

    var detailChatTableName = "DetailChat-6tai4sqoubaihizrtoyvo7a5da-dev";

    console.log(userId);
    console.log(friendId);
    console.log(uploadTime);


    var bucketname = "developcall-transcribe-output"
    var key = jobName + ".json"





    var date = new Date();

    var dateString  = date.toISOString();
    var lastchangedat = date.getTime();

    var newId = userId + friendId + uploadTime;





    var params = {
        TableName : tablename,
        Item :
            {
                     "userID" : userId,
                                   "friendID" : friendId,
                                   "id" : uid,
                                    "date" : uploadTime,
                                    "s3_url" : fileUrl,
                                    "keyWord" : "",
                                    "summary" : "",
                                    "memo" : "",
                                    "_lastChangedAt" : lastchangedat,
                                    "createdAt" : dateString,
                                    "updatedAt" : dateString,
                                    "_version" : 1,

            }

    }


    var lastContactParams = {
        TableName : friendTableName,
        Key : {
            "id" : friendId,
        },
        UpdateExpression : "set lastContact = :c",
        ExpressionAttributeValues:{
            ":c" : uploadTime,
        },
        ReturnValues:"UPDATED_NEW"
    };

    var transcriptParams = {
            Bucket : "developcall-transcribe-output",
            Key : jobName+".json",
    };


    async function createItem(){
    try {
        await docClient.put(params).promise();

    } catch (err) {
        return err;
        }
    }

    async function updateContact()
    {
        try{
            await docClient.update(lastContactParams).promise();
        }
        catch(err)
        {
        console.log(err);
        }
    }




    async function updateScript()
    {
        try {
        const  data = await s3.getObject(transcriptParams).promise();
        let parseJson = JSON.parse(data.Body.toString());
        var script  = parseJson.results.transcripts[0].transcript;
        getScript = script;
        var segment = parseJson.results.speaker_labels.segments;
        var mainitem = parseJson.results.items;

        var item1 = new Array();
        var item2= new Array();
        for (var items = 0 ; items< segment.length; items++) {


            for( var result = 0 ; result < segment[items].items.length; result++)
            {
                console.log(segment[items].items[result]);
                var start  = segment[items].items[result].start_time;
                var end = segment[items].items[result].end_time;
                var sp = segment[items].items[result].speaker_label;
                item1.push(new Items(start, end, sp , ""));
            }

        }

        for( var value = 0; value < mainitem.length; value++)
        {

            var start = mainitem[value].start_time;
            var end = mainitem[value].end_time;


            for( var innervalue = 0; innervalue < mainitem[value].alternatives.length; innervalue++)
            {
                var content = mainitem[value].alternatives[innervalue].content;
                var reg = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi
                if(typeof start == "undefined")
                {

                } else
                {
                item2.push(new Items(start,end,"null",content));
                }



            }
        }




       var array = new Array();
       var final = mergeArray(item1,item2,array);


        try{
        await callBack(final);
        }
        catch (err)
        {
        console.log(err);
        }



        var scriptparam = {
            TableName : tablename,
                    Key : {
                        "id" : uid,
                    },
                    UpdateExpression : "set summary = :s",
                    ExpressionAttributeValues:{
                        ":s" : script,
                    },
                    ReturnValues:"UPDATED_NEW"
        };
        try {
            await updateDbScript(scriptparam);
        }
        catch ( err )
        {
            console.log(err);
        }


        }
        catch (err) {
            console.log(err);
        }
    }

    function mergeArray(item1, item2 , array)
    {


        var pre;
        for(var i =0 ; i<item1.length; i++)
        {

            item1[i].content = item2[i].content;
            array.push(item1[i]);
        }
        pre = array[0].spk;

        var count =1;
        for(var i= 1; i< array.length; i++)
        {
           if(array[i].spk == pre)
           {


           } else
           {
            pre = array[i].spk;

            count++;
           }
        }
        var result = new Array(count);
        for ( var i = 0; i< count; i++)
        {
         result[i] = new Array(2);
         result[i].fill("");
        }



        count =0;
        var newContent = "";
        pre = array[0].spk;
        for(var i=0; i<array.length; i++)
        {
            if(array[i].spk == pre)
            {

            result[count][0] = pre;

            result[count][1] += array[i].content + " ";

            }
            else {



                pre = array[i].spk;

                count++;

                result[count][0] = pre;

                result[count][1] += array[i].content;

            }
        }

        return result;

    }



    function createDetailChat(pr)
    {

            docClient.put(pr).promise();

    }

    async function callBack(result)
    {


        try{
                for(var i = 0; i<result.length; i++)
                {
                               console.log(result[i]);
                    var detailParams = {
                        TableName : detailChatTableName,
                        Item :
                        {
                            "chatID" : uid,
                            "id" : randomUUID(),
                            "start_time" : "",
                            "end_time" : "",
                            "speaker_label" : result[i][0],
                            "content" : result[i][1],
                            "_lastChangedAt" : new Date().getTime(),
                            "createdAt" : new Date().toISOString(),
                            "updatedAt" : new Date().toISOString(),
                            "_version" : 1,
                        }

                    }

                    createDetailChat(detailParams);
                }

        }
        catch(err)
        {
        console.log(err);
        }

    }
    async function updateDbScript(scriptparam)
    {
        try{
                    await docClient.update(scriptparam).promise();
                }
                catch(err)
                {
                console.log(err);
                }
    }







    async function callLambda()
    {
        try{

             const newEvent = {
                url : fileUrl,
                summary : getScript,
                job : jobName,
                id : uid

             };

                var callLambdaParams = {
                FunctionName : 'getkeywordtest',
                  InvocationType : 'Event',
             LogType : 'None' ,
             Payload : JSON.stringify(newEvent, null, 4)
             };
            await lambda.invoke(callLambdaParams).promise();
        }
        catch(err)
        {
            console.log(err);
        }
    }





    try{
        await createItem();
        await updateContact();
        await updateScript();
        await callLambda();
        console.log("success");




    }
    catch(err)
    {
        console.log(err);
    }








};
