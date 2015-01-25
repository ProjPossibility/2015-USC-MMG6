var MAX_SCORE = 2;

Parse.Cloud.define("join", function(request, response) {
  // count how many existing players
  // add a new player object (playerNumber = count + 1, score = 0)
  // return the player count + 1

  var query = new Parse.Query("Player");
  query.find({
    success: function(results) {
      var Player = new Parse.Object.extend("Player");
      var newPlayer= new Player();

      newPlayer.set("playerNumber", results.length + 1);
      newPlayer.set("score", 0);
      newPlayer.set("ready", false);

      newPlayer.save();

      response.success(results.length + 1);
    },
    error: function() {
      response.success(-1);
    }
  });
});

// Send back a list of all the player numbers
Parse.Cloud.define("get_players", function(request, response) {
  var query = new Parse.Query("Player");
  query.find({
    success: function(results) {
      var responseObject = [];
      for(var i in results) {
        responseObject.push(results[i].get("playerNumber"));
      }

      response.success(responseObject);
    },
    error: function() {

    }
  });
});

// request params: playerNumber
Parse.Cloud.define("ready", function(request, response) {

  // request contains user id; set that user's ready-state to be true
  // if not everyone's ready, return {response: "wait"}
  // else if the maximum score == MAX_SCORE, return list of scores
  // else create instruction if haven't already, then return it

  var query = new Parse.Query("Player");
  var playerNumber = request.params.playerNumber;
  query.equalTo("playerNumber", playerNumber);
  query.find({
    success: function(results) {
      if(results.length > 0) {
        results[0].set("ready", true);
        results[0].save();
      }

    },
    error: function() {

    }
  });

  var playerCountQuery = new Parse.Query("Player");
  playerCountQuery.find({
    success: function(results) {

      // determine if all are ready and if max score reached
      var allReady = true;
      var maxScore = 0.0;
      for(var i = 0; i < results.length; i++) {
        if(!results[i].get("ready")) {
          allReady = false;
        }
        if(maxScore < results[i].get("score")) {
          maxScore = results[i].get("score");
        }
      }



      // ================ Respond with "wait" ================
      if(!allReady) {

        response.success({
          response: "wait",
          maxScore: maxScore,
          allReady: allReady
        });
      }



      // ================= Respond with "end" =================
      else if(maxScore >= MAX_SCORE) {
        var responseObj = {
          response: "end",
          players: []
        };  
        for(var i = 0; i < results.length; i++) {
          responseObj.players.push({
            playerNumber: results[i].get("playerNumber"),
            score: results[i].get("score")
          });
        }
        response.success(responseObj);
      }



      // ================ Respond with "instruction" ==================
      else {
        var queryInstr = new Parse.Query("Instruction");

        queryInstr.find({
          success: function(results) {
            // Check if an instruction already exists
            if(results.length > 0) {
              // Build responseObj
              var responseObj = {
                response: "instruction",
                simonSays: results[0].get("simonSays"),
                forEveryone: results[0].get("forEveryone"),
                who: results[0].get("who"),
                action: results[0].get("action"),
                timeStamp: results[0].get("timeStamp")
              }

              response.success(responseObj);
            }
            else {

              // Build responseObj
              var responseObj = {
                response: "instruction",
                simonSays: simonSays(),
                forEveryone: forEveryone(),
                who: who(),
                action: actionNumber(),
                timeStamp: getTime()
              }

              // Build newInstruction
              var Instruction = Parse.Object.extend("Instruction");
              var newInstruction = new Instruction();
              newInstruction.set("simonSays", responseObj.simonSays);
              newInstruction.set("forEveryone", responseObj.forEveryone);
              newInstruction.set("who", responseObj.who);
              newInstruction.set("action", responseObj.action);
              newInstruction.set("timeStamp", responseObj.timeStamp);

              // Save newInstruction and return responseObj
              newInstruction.save(null, {
                success: function() {
                  response.success(responseObj);
                }
              });
            }
          },
          error: function(obj, error) {
            
          }
        });
      }
    }
  });

});

// Delete instruction, if currently existing
// Tell the back-end whether it's success/fail
// request params: playerNumber, isSuccess
Parse.Cloud.define("result", function(request, response) {
  var queryInstr = new Parse.Query("Instruction");
  queryInstr.find({
    success: function(results) {
      if(results.length > 0) {
        results[0].destroy();
      }
    },
    error: function(obj, error) {
      
    }
  });

  var query = new Parse.Query("Player");
  var playerNumber = request.params.playerNumber;

  // Set everyone's ready property to false
  query.find({
    success: function(results) {
      for(var i in results) {
        results[i].set("ready", false);
        results[i].save();
      }
    }
  });

  query.equalTo("playerNumber", playerNumber);
  query.find({
    success: function(results) {
      if(results.length > 0) {
        if(request.params.isSuccess) {
          var currScore = results[0].get("score");
          results[0].set("score", currScore + 1);
        }
        results[0].save();
      }
      response.success({recieved: true});
    },
    error: function() {

    }
  });
});

// Current player is deleted
// If that was the last player, delete the instruction as well
// request params: playerNumber
Parse.Cloud.define("leave", function(request, response) {
  var queryPlayer = new Parse.Query("Player");
  queryPlayer.equalTo("playerNumber", request.params.playerNumber);
  queryPlayer.find({
    success: function(results) {
      if(results.length > 0) {
        results[0].destroy({
          success: function() {
            queryPlayer = new Parse.Query("Player");
            queryPlayer.find({
              success: function(results) {
                if(results.length == 0) {
                  var queryInstr = new Parse.Query("Instruction");
                  queryInstr.find({
                    success: function(results) {
                      if(results.length > 0) { // found an instruction, so delete it
                        results[0].destroy( {
                          success: function() {
                            response.success({});
                          },
                          error: function() {

                          }
                        })
                      }
                      else { // no instructions found, so just return
                        response.success({});
                      }
                    },
                    error: function(obj, error) {
                      
                    }
                  });
                }
              },
              error: function(obj, error) {

              }
            });
          },
          error: function(obj, error) {

          }
        });
      }
    },
    error: function(obj, error) {
      
    }
  });
});

function simonSays() {
	var generated = Math.random();
	if (generated < 0.8) {
		//do a simon says
		return true;
	}
	else{
		return false;
	}
}

function forEveryone() {
  return false; //TODO change this to return randomly chosen true or false, perhaps 60% true
}

function who() {
  return [1,2,3,4]; //TODO change this to chose a subset of them
}

function actionNumber() {
	var generated = Math.random()*4;
	return Math.floor(generated);
}

function getTime() { 
	var date = new Date().getTime();
	return date + 5000;
}