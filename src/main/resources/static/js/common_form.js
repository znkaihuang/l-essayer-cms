const imageFile = document.getElementById('imageFile');
const image = document.getElementById('image');
imageFile.addEventListener("change", function() {
	var file = imageFile.files[0];
    var reader = new FileReader();
    reader.onload = (function() {
		return function(e) {
			image.src = e.target.result;
		};
	}) ();
    reader.readAsDataURL(file);
});
	

let modalTitle = "Error";
let modalBody;
const userForm = document.getElementById('userForm');
userForm.addEventListener('submit', function(event) {
	
	const email = document.getElementById('email').value;
	
	console.log(email + ' ' + userId);
	if (userId === null || (userId !== null && email != userEmail)) {
		event.preventDefault();
		let data = {userId: userId, email: email};
	
		response = fetch(checkUniqueEmailURL, {
			method: 'POST', // or 'PUT'
			body: JSON.stringify(data) // data can be `string` or {object}!
		}).then(res => res.text())
			.catch(error => console.error('Error:', error))
			.then(response => {
				console.log('Success:', response);
				if (response === 'true') {
	
					userForm.submit();
				}
				else {
	
					showModal();
	
					document.getElementById('messageModalTitle').innerText = modalTitle;
					document.getElementById('messageModalBody').innerText = 'The email ' + email + 'has already existed!';
	
				}
			});
		}
});
