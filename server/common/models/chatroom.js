var loopback = require('loopback');

module.exports = function(ChatRoom){
  'use strict',
  ChatRoom.join = function(roomId, ctx, cb) {
    console.log(ctx.req);
    if (!ctx.req.accessToken) {
      console.log("No access token found on request");
      throw new Error(401, "You need to login");
    }
    var userId = ctx.req.accessToken.userId;
    var Room = {}
    var query = {
      id: roomId
    };
    ChatRoom.findOne({where: query})
      .then(function(room) {
        if (room === undefined) {
          throw new Error(404, "No room found");
        }
        console.log(room)
        Room = room;

        var query = {
          id: userId
        };
        return ChatRoom.app.models.member.findOne({where: query})
      }).then(function(member) {

        /* Check user is already in the room */
        var isNewMember = true;
        for (var i = 0; i < Room.participant.length; i++) {
          if (Room.participant[i].id.equals(userId)) {
            isNewMember = false;
            break;
          }
        }

        /* If not found memeber with id and add it */
        if(isNewMember) {
          Room.participant.push(member);
        }
        return ChatRoom.upsert(Room);
      }).then(function(room, err) {
        if(err) {
          throw err;
        }
        var filter = {
          limit: 2,
          order: 'createdOn DESC'
        };
        /* To be refactored (add skip to pagenation) */
        return ChatRoom.app.models.Message.find(filter)
        // cb(null, room);
      }).then(function(messages, err) {
        if(err) {
          throw err;
        }
        console.log(messages);
        cb(null, messages);
        /* To be refactored
         * message type will be
         * {
         *   "next": "url to next",
         *   "data": array of messages"
         * }*/
      }).catch(function(err) {
        cb(err);
      });
  };

  ChatRoom.remoteMethod(
    'join',
    {
      description: 'Join into created session',
      accepts: [
        {
          arg: "roomId",
          type: "string",
          http: {source: "query"}
        },
        {
          arg: "ctx",
          type: "object",
          http: {source: "context"}
        }
      ],
      returns: {
        arg: 'messages', type: 'object', root: true,
        description:
          'The response body contains top 100 messages'
      },
      http: {verb: 'get'}
    }
  );
}
