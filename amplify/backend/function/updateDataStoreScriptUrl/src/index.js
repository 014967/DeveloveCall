

var aws = require('aws-sdk');
var docClient = new aws.DynamoDB.DocumentClient();



exports.handler = async (event) => {
    // TODO implement




// get id from event
    var jobName = event.detail.TranscriptionJobName;
    var removeFormatString = jobName.split(".");

    //var dirtyNumber =  /(.*)(-.+)(-.+)(?<=$)/.exec(removeFormatString[0]);
    var dirtyNumber = removeFormatString[0].split("_");
    console.log(dirtyNumber[0]);
    console.log(dirtyNumber[1]);
    console.log(dirtyNumber[2]);

    /*
    var userid = dirtyNumber[1];
    var number = dirtyNumber[2];
    var date = dirtyNumber[3];
    number = number.replace(/-/g , "");
////////////////////
    var tablename = "Chat-elly5d4s7ng2lmypvuhwgpr6lm-dev";

    var getparams ={
        TableName : tablename,
        Key : {
            "number" : number,
        }

    }
    docClient.get(getparams , function(err, data)
    {
        if(err)
        {
             console.error("Unable to read item. Error JSON:", JSON.stringify(err, null, 2));
        }
        else
        {
            console.log("getItem succeded: " , data);
            console.log("GetItem succeeded:", JSON.stringify(data, null, 2));
        }

    })
*/
//get access dynamo tablename

/*

    var params = {
        TableName : tablename,
        Item :
            {
                     "userID" : userid,
                                   "friendID" : ,
                                    "id" : ,
                                    "date" : ,
                                    "s3_url" : ,
                                    "keyWord" : ,
                                    "summary" : ,
                                    "memo" : ,
            }

    }


    docClient.put(params, function(err, data) {
        if (err) {
            console.error("Unable to add item. Error JSON:", JSON.stringify(err, null, 2));
        } else {
            console.log("Added item:", JSON.stringify(data, null, 2));
        }
    });


*/









};
