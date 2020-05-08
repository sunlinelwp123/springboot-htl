$(document).ready(function () {
    token = $.cookie("token");
    userId = $.cookie("userid");
    console.log(userId, token);
    if (token == null || userId == null) {
        return  window.location.href = "/htllogin/register/html";
        //return;
    }
    var data = {};
    data.id = userId;
    data.token = token;
    var reqData;
    htl.ajaxHtl("/htlUser/getUser", data, "post", function (data) {
        if (data.code === 200) {
            var user = data.data;
            $.cookie("user.headUrl", user.photourl,{ expires: 30, path: '/' });
            $.cookie("user.nickName", user.nickName,{ expires: 30, path: '/' });
            $.cookie("user.userName", user.userName,{ expires: 30, path: '/' });
        } else {
            console.log(data);
        }
    }, function (data) {
        console.log(data);
    }, "json", true);
});