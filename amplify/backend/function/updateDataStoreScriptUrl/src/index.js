

var aws = require('aws-sdk');
var docClient = new aws.DynamoDB.DocumentClient();



exports.handler = async (event) => {
    // TODO implement




// get id from event
    var jobName = event.detail.TranscriptionJobName;
    var removeFormatString = jobName.split(".");

    //var dirtyNumber =  /(.*)(-.+)(-.+)(?<=$)/.exec(removeFormatString[0]);
    var dirtyNumber = removeFormatString[0].split("_");


    var userId = dirtyNumber[0];
    var friendId = dirtyNumber[1];
    var uploadTime = dirtyNumber[2];
    var fileUrl = "s3://developcall-transcribe-output/" + jobName + ".json"

    var tablename = "Chat-6tai4sqoubaihizrtoyvo7a5da-dev";

    var friendTableName = "Friend-6tai4sqoubaihizrtoyvo7a5da-dev";

    console.log(userId);
    console.log(friendId);
    console.log(uploadTime);


    var params = {
        TableName : tablename,
        Item :
            {
                     "userID" : userId,
                                   "friendID" : friendId,
                                   "id" : jobName,
                                    "date" : uploadTime,
                                    "s3_url" : fileUrl,
                                    "keyWord" : "",
                                    "summary" : "",
                                    "memo" : "",
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




    try{
        await createItem();
        await updateContact();
        console.log("success");

    }
    catch(err)
    {
        console.log(err);
    }








};
