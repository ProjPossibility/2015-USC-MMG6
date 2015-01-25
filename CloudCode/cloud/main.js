
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  // var jsonResponse = {
  //   hello: "SS12",
  //   world: "Simon Says"
  // };
  // response.success(jsonResponse);
  response.success("SS12! ACM!");
});

function simonSays() {
	double generated = Math.random();
	if( generated > 0.8){
		//do a simon says
		return true;
	}
	else{
		return false;
	}
}

function Everyone() {
	double generated = Math.random();
	if( generated > 0.7){
		//do everyone
		return true;
	}
	else{
		return false;
	}
}

function actionNumber() {
	double generated = Math.random()*16.0;
	return generated;
}


