
var aws = require('aws-sdk');
var ddb = new aws.DynamoDB();

exports.handler = async (event , context) => {
    // TODO implement


    let date = new Date();

    if(event.request.userAttributes.sub)
    {

        let params = {
            Item : {

            'id' : {S: event.request.userAttributes.id},
            'name' : {S : event.request.userAttributes.name},
            'createdAt': {S: date.toISOString()},
            'updatedAt': {S: date.toISOString()},


            },
            TableName: 'USER_TABLE'

        };


        try {
                    await ddb.putItem(params).promise()
                    console.log("Success");
                } catch (err) {
                    console.log("Error", err);
                }

                console.log("Success: Everything executed correctly");
                context.done(null, event);



    }
    else
    {
        console.log("Error: Nothing was written to DynamoDB");
        context.done(null, event);
    }


};
