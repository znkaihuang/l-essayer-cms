function displayImageFile() {
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
}

function checkUniqueEmail() {
	
	const userForm = document.getElementById('userForm');
	userForm.addEventListener('submit', function(event) {
		
		const email = document.getElementById('email').value;
		
		if (userId === null || (userId !== null && email != userEmail)) {
			event.preventDefault();
			let data = {userId: userId, email: email};
		
			response = fetch(checkUniqueEmailURL, {
				method: 'POST',
				body: JSON.stringify(data)
			}).then(res => res.text())
				.catch(error => console.error('Error:', error))
				.then(response => {
					if (response === 'true') {
						userForm.submit();
					}
					else {
						let modalTitle = "Error";
						showModal(modalTitle);
						document.getElementById('messageModalTitle').innerText = modalTitle;
						document.getElementById('messageModalBody').innerText = 'The email ' + email + ' has already existed!';
		
					}
				});
			}
	});
}

function validateArticleFormInputs () {
	const articleForm = document.getElementById('articleForm');
	articleForm.addEventListener('submit', function(event) {
		event.preventDefault();
		
		let warningMessage = 'Please fill in the following field(s):';
		let blankFieldCount = 0;
		let title = document.getElementById('title');
		let author = document.getElementById('author');
		let date = document.getElementById('published-date');
		let summary = document.getElementById('summary');
		let content = document.getElementById('content');
		
		if (title.value === '' || title.value == null) {
			warningMessage += '\n  - title';
			blankFieldCount++;
		}
		if (author.value === '' || author.value == null) {
			warningMessage += '\n  - author';
			blankFieldCount++;
		}
		if (date.value === '' || date.value == null) {
			warningMessage += '\n  - date';
			blankFieldCount++;
		}
		if (summary.value === '' || summary.value == null) {
			warningMessage += '\n  - summary';
			blankFieldCount++;
		}
		if (content.value === '' || content.value == null) {
			warningMessage += '\n  - content';
			blankFieldCount++;
		}
				
		if (blankFieldCount == 0) {
			articleForm.submit();
		}
		else {
			showModal('Warning');
			document.getElementById('messageModalTitle').innerText = 'Warning';
			document.getElementById('messageModalBody').innerText = warningMessage;
		}
	});
	
}

function createTagInTagsSection(tag) {
	if (canAddTag(addedTagsDiv, tag.value)) {
		tagElement = createSpanEleForTag(tag.value, addedTagsDiv);
		addedTagsDiv.appendChild(tagElement);
	}
	tag.value = '';
}

function createSpanEleForTag(tagName, addedSection) {
	tagElement = document.createElement('span');
	tagElement.innerText = tagName;
	tagElement.classList.add('badge');
	tagElement.classList.add('rounded-pill');
	tagElement.classList.add('fs-6');
	tagElement.classList.add('my-1');
	tagElement.classList.add('mx-1');
	tagElement.onclick = function() {
		addedSection.removeChild(this);
	}
	return tagElement;
}

function canAddTag(addedSection, tagName) {
	if (addedSection.childElementCount >= 6)
		return false;
	
	for (child of addedSection.children) {
		if (child.innerText === tagName)
			return false;
	}
	
	return true;
}