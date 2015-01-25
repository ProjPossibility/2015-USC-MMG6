var MAX_SCORE = 10;

// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("get_instruction", function(request, response) {
  var jsonResponse = {
    simonSays: simonSays(),
    who: everyone(),
    action: actionNumber(),
    timeStamp: getTime()    
  };
  response.success(jsonResponse);
});

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

// params: playerNumber
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

      // if not all ready, respond with "wait"
      if(!allReady) {
        response.success({
          response: "wait",
          maxScore: maxScore,
          allReady: allReady
        });
      }
      else if(maxScore >= MAX_SCORE) {
        var jsonObj = {
          response: "end",
          players: []
        };
        for(var i = 0; i < results.length; i++) {
          jsonObj.players.push({
            playerNumber: results[i].playerNumber,
            score: results[i].score
          });
        }
        response.success(jsonObj);
      }
      else {
        var queryInstr = new Parse.Query("Instruction");

        queryInstr.get("0", {
          success: function(theInstruction) {
            response.success(theInstruction);
          },
          error: function(obj, error) {
            var Instruction = Parse.Object.extend("Instruction");
            var currInstruction = new Instruction();

            currInstruction.set("simonSays", simonSays());
            currInstruction.set("who", everyone());
            currInstruction.set("action", actionNumber());
            currInstruction.set("timeStamp", getTime());

            // Note: hard-coded object ID
            
            currInstruction.set("objectId", "0");
            currInstruction.save(null);
            response.success(currInstruction);
          }
        });
      }
    }
  });

});

// Tell the back-end whether it's success/fail
// Also delete instruction, if currently existing
// request params: playerNumber, isSuccess
Parse.Cloud.define("result", function(request, response) {
  var query = new Parse.Query("Player");
  var playerNumber = request.params.playerNumber;
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

Parse.Cloud.define("leave", function(request, response) {
  
});

function simonSays() {
	var generated = Math.random();
	if (generated > 0.8) {
		//do a simon says
		return true;
	}
	else{
		return false;
	}
}

function everyone() {
	return [1,2,3,4];
}

function actionNumber() {
	// var generated = Math.random()*15.0;
	// return generated;
	return 1;
}

function getTime() { 
	var date = new Date().getTime();
	return date + 5000;
}