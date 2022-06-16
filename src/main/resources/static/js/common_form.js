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
		if (addedTagsDiv.childElementCount < 2) {
			warningMessage += '\n  - tag(s)';
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

function createHiddenInputEleForExistedTags(addedTagsDiv) {
	
	for (element of addedTagsDiv.childNodes) {
		if (element.nodeName === "SPAN") {
			hiddenEle = createHiddenInputEleForTag(element.innerText);
			addedTagsDiv.appendChild(hiddenEle);
			addOnClickRemoveFunctionForTag(element, hiddenEle);
		}
	}
}

function addOnClickRemoveFunctionForTag(tag, hidden) {
	tag.onclick = function() {
		addedTagsDiv.removeChild(this);
		addedTagsDiv.removeChild(hidden);
	}
}

function createTagInTagsSection(tag) {
	if (canAddTag(addedTagsDiv, tag.value)) {
		hiddenEle = createHiddenInputEleForTag(tag.value);
		tagElement = createSpanEleForTag(tag.value, addedTagsDiv, hiddenEle);
		addedTagsDiv.appendChild(tagElement);
		addedTagsDiv.appendChild(hiddenEle);
	}
	tag.value = '';
}

function createSpanEleForTag(tagName, addedSection, hiddenEle) {
	tagElement = document.createElement('span');
	tagElement.innerText = tagName;
	tagElement.classList.add('badge');
	tagElement.classList.add('rounded-pill');
	tagElement.classList.add('fs-6');
	tagElement.classList.add('my-1');
	tagElement.classList.add('mx-1');
	tagElement.onclick = function() {
		addedSection.removeChild(this);
		addedSection.removeChild(hiddenEle);
	}
	return tagElement;
}

function createHiddenInputEleForTag(tagName) {
	tagElement = document.createElement('input');
	tagElement.type = 'hidden';
	tagElement.name = 'tag';
	tagElement.value = tagName;
	return tagElement;
}

function canAddTag(addedSection, tagName) {
	if (addedSection.childElementCount >= 11)
		return false;
	
	for (child of addedSection.children) {
		if (child.innerText === tagName)
			return false;
	}
	
	return true;
}

function createClickRemoveFileFunction() {
	let addedFileSection = document.getElementById('added-files-section');
	let imageFileArrayEle = document.getElementById('imageFiles');
	for (element of document.getElementsByClassName('remove-file')) {
		element.onclick = function() {
			removeFile = this.id.replace('removeFile', '');
			addedFileSection.removeChild(document.getElementById('file' + removeFile));
			imageFileArrayEle.value = imageFileArrayEle.value.replace(removeFile, '');
			if (addedFileSection.childElementCount < 2) {
				addedFileSection.remove();
			}
		}
	}

}

function validateVideoFormInputs () {
	const videoForm = document.getElementById('videoForm');
	videoForm.addEventListener('submit', function(event) {
		event.preventDefault();
		
		let warningMessage = 'Please fill in the following field(s):';
		let blankFieldCount = 0;
		let title = document.getElementById('title');
		let lecturer = document.getElementById('lecturer');
		let date = document.getElementById('uploaded-date');
		let uploader = document.getElementById('uploader');
		let languages = document.getElementsByName('language');
		let hasSubtitle = document.getElementsByName('hasSubtitle');
		let description = document.getElementById('description');
		let uploadedVideo = document.getElementById('uploadedVideo');
		let videoLength = document.getElementById('length');
		
		if (title.value === '' || title.value == null) {
			warningMessage += '\n  - title';
			blankFieldCount++;
		}
		if (lecturer.value === '' || lecturer.value == null) {
			warningMessage += '\n  - lecturer';
			blankFieldCount++;
		}
		if (date.value === '' || date.value == null) {
			warningMessage += '\n  - uploaded date';
			blankFieldCount++;
		}
		if (uploader.value === '' || uploader.value == null) {
			warningMessage += '\n  - uploader';
			blankFieldCount++;
		}
		if (description.value === '' || description.value == null) {
			warningMessage += '\n  - description';
			blankFieldCount++;
		}
		if (!validateRadios(languages)) {
			warningMessage += '\n  - language';
			blankFieldCount++;
		}
		if (!validateRadios(hasSubtitle)) {
			warningMessage += '\n  - subtitle';
			blankFieldCount++;
		}
		if (uploadedVideo.value === '' || uploadedVideo.value == null) {
			warningMessage += '\n  - uploaded video';
			blankFieldCount++;
		}
		if (videoLength.value === '' || videoLength.value == null) {
			warningMessage += '\n  - video length';
			blankFieldCount++;
		}
		if (addedTagsDiv.childElementCount < 2) {
			warningMessage += '\n  - tag(s)';
			blankFieldCount++;
		}
		
		if (blankFieldCount == 0) {
			videoForm.submit();
		}
		else {
			showModal('Warning');
			document.getElementById('messageModalTitle').innerText = 'Warning';
			document.getElementById('messageModalBody').innerText = warningMessage;
		}
	});
	
}

function validateRadios(radioElements) {
	let flag = false;
	for (radio of radioElements) {
		if (radio.checked) {
			flag = true;
		}
	}
	
	return flag;
}