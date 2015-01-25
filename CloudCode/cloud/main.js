
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  var jsonResponse = {
    simonSays: simonSays(),
    who: everyone(),
    action: actionNumber(),
    timeStamp: getTime()    
  };
  response.success(jsonResponse);
});

function simonSays() {
	var generated = Math.random();
	if( generated > 0.8){
		//do a simon says
		return true;
	}
	else{
		return false;
	}
}

function everyone() {
	var generated = Math.random();
	if( generated > 0.7){
		//do everyone
		return true;
	}
	else{
		//get users still in the game

		return false;
	}
}

function actionNumber() {
	var generated = Math.random()*16.0;
	return generated;
}

function getTime() { 
	return new Date().getTime(); 
}


