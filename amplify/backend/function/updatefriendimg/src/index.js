var aws = require("aws-sdk");
var docClient = new aws.DynamoDB.DocumentClient();
exports.handler = async (event) => {
    // TODO implement

    const eventRecord =  event.Records[0];

    const imgpath = eventRecord.s3.object.key;
    const keyArray = eventRecord.s3.object.key.split('_');

    const userid = keyArray[0];
    const groupid = keyArray[1];
    const dummyArray = keyArray[2].split('.');
    const friendid = dummyArray[0];



    console.log(userid);
    console.log(groupid);
    console.log(friendid);

    const tablename = "Friend-6tai4sqoubaihizrtoyvo7a5da-dev";
     var params = {
        TableName : tablename,
        Key: {
            "id" : friendid,

        },
        UpdateExpression : "set friendImg = :f",
        ExpressionAttributeValues:{
          ":f" : imgpath,
        },
        ReturnValues:"UPDATED_NEW"

    };


     async function updateItem(){
    try {
        await docClient.update(params).promise();
    } catch (err) {
        return err;
        }
    }



    try{
        await updateItem();

            console.log("success");

    }
    catch(err)
    {
        console.log(err);
    }
};
