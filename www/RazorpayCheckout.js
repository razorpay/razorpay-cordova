/*global cordova, module*/

module.exports = {
    open: function (options, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Checkout", "open", [JSON.stringify(options)]);
    },
    openWithAdditionalData: function (options, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Checkout", "openWithAdditionalData", [JSON.stringify(options)]);
    }
};
