
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
  var jsonResponse = {
    hello: "SS12",
    world: "Simon Says"
  };
  response.success(jsonResponse);
  // response.success("SS12! ACM!");
});
