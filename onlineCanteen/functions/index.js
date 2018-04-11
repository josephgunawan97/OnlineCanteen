// imports firebase-functions module
const functions = require('firebase-functions');

// imports firebase-admin module
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{user_id}/{notification_id}').onWrite(event =>{
    const user_id = event.params.user_id;
    const notification = event.params.notification;

    console.log('Sent a notification to : ', user_id);

    if(!event.data.val()){
        return console.log('A Notification has been deleted from the database : ', notification_id);
    }

    const deviceToken = admin.database().ref(`/store/${user_id}/device_token`).once('value');

    return deviceToken.then(result =>{
        const token_id = result.val();

        const payload = {
            notification: {
                title : "Sit 'n Shop",
                body : "You have a new order!",
                icon : "logo",
                sound: "default"
            }
        };

        return admin.messaging().sendToDevice(token_id, payload);
    });

});

exports.sendOrderNotification = functions.database.ref('/ordernotifications/{user_id}/{notification_id}').onWrite(event =>{
    const user_id = event.params.user_id;
    const notification = event.params.notification;

    console.log('Sent order notification to : ', user_id);

    if(!event.data.val()){
        return console.log('A Notification has been deleted from the database : ', notification_id);
    }

    const deviceToken = admin.database().ref(`/users/${user_id}/device_token`).once('value');

    return deviceToken.then(result =>{
        const token_id = result.val();

        const payload = {
            notification: {
                title : "Sit 'n Shop",
                body : "Your order has been accepted!",
                icon : "logo",
                sound: "default"
            }
        };

        return admin.messaging().sendToDevice(token_id, payload);
    });

});

exports.sendOrderDeclineNotification = functions.database.ref('/orderdeclinenotifications/{user_id}/{notification_id}').onWrite(event =>{
    const user_id = event.params.user_id;
    const notification = event.params.notification;

    console.log('Sent order decline notification to : ', user_id);

    if(!event.data.val()){
        return console.log('A Notification has been deleted from the database : ', notification_id);
    }

    const deviceToken = admin.database().ref(`/users/${user_id}/device_token`).once('value');

    return deviceToken.then(result =>{
        const token_id = result.val();

        const payload = {
            notification: {
                title : "Sit 'n Shop",
                body : "Your order has been declined!",
                icon : "logo",
                sound: "default"
            }
        };

        return admin.messaging().sendToDevice(token_id, payload);
    });

});