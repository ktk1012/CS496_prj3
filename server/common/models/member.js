var AWS = require('aws-sdk');
var fs = require('fs');

var s3 = new AWS.S3();

module.exports = function(Member) {
  Member.beforeRemote('create', function(ctx, unused, next) {
    console.log(ctx.res);
  });
};
