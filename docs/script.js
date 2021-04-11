firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
    // User is signed in.

    var user = firebase.auth().currentUser;

    if(user != null){
    	var email_id = user.email;
    	var email_verified = user.emailVerified;

    	if(email_verified){
    		document.getElementById("verify_btn").style.display = "none";
    	}
    }

  } else {
    // No user is signed in.

  }
});

function login(){
	var orgEmail = document.getElementById("email").value;
	var orgPass = document.getElementById("password").value;

	firebase.auth().signInWithEmailAndPassword(orgEmail, orgPass)
	  .then((userCredential) => {
	    // Signed in
	    var user = userCredential.user;

	    return "/data.html";
	    // ...
	})
	.catch((error) => {
	    var errorCode = error.code;
 	    var errorMessage = error.message;

 	    window.alert("Error: " + errorMessage);
	});
}

function register(){
	var orgName = document.getElementById("org_name").value;
	var orgType = document.getElementById("org_type").value;
	var orgEmail = document.getElementById("email").value;
	var orgPass = document.getElementById("password").value;

	firebase.auth().createUserWithEmailAndPassword(orgEmail, orgPass)
	  .then((userCredential) => {
	    // Signed in
	    var user = userCredential.user;

	    return "/data.html";
	    // ...
	})
	.catch((error) => {
	    var errorCode = error.code;
 	    var errorMessage = error.message;

 	    window.alert("Error: " + errorMessage);
	});
}

function send_verification(){
	var user = firebase.auth().currentUser;

	user.sendEmailVerification().then(function() {
	  // Email sent.

	  window.alert("Verificatio sent");

	}).catch(function(error) {
	  // An error happened.

	  window.alert("Error: " + error.message);
	});
}