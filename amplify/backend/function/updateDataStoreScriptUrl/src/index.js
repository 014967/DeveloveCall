

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

    var tablename = "Chat-elly5d4s7ng2lmypvuhwgpr6lm-dev";



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



    async function createItem(){
    try {
        await docClient.put(params).promise();
    } catch (err) {
        return err;
        }
    }




    try{
        await createItem();

            console.log("success");

    }
    catch(err)
    {
        console.log(err);
    }








};
