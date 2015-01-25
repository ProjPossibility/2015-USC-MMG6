
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

});

Parse.Cloud.define("ready", function(request, response) {
  var Instruction = Parse.Object.extend("Instruction");
  var query = new Parse.Query(Instruction);

  query.get("0", {
    success: function(theInstruction) {

    },
    error: function(obj, error) {

    }
  });
  var currInstruction = new Instruction();

  currInstruction.set("simonSays", simonSays());
  currInstruction.set("who", everyone());
  currInstruction.set("action", actionNumber());
  currInstruction.set("timeStamp", getTime());

  // Note: hard-coded object ID
  
  currInstruction.set("objectId", "0");


});

Parse.Cloud.define("result", function(request, response) {

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