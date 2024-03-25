// to be used in forms that need password checking
function pwdValidation(formId, passwordId, confirmPasswordId, messageId) {
	let pwd = document.getElementById(passwordId);
  	let confirmPwd = document.getElementById(confirmPasswordId);
  	let msg = document.getElementById(messageId);
  	
  	document.getElementById(formId).className='submitted';
  	
  	const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
	
	if(!regex.test(pwd.value)) {
		pwd.setCustomValidity('Minimum eight characters, at least one letter, one number and one special character');
	} else {
		pwd.setCustomValidity('');
	}

  	if (pwd.value != confirmPwd.value) {
		msg.classList.add('pwd-message');
    	msg.textContent='*Passwords do not match';
    	confirmPwd.setCustomValidity('Match passwords');
  	} else {
		msg.textContent='';
    	msg.classList.remove('pwd-message');
    	confirmPwd.setCustomValidity('');
  	}
}