/**
 * 
 */

function selectModal(modalTitle) {
	if (modalTitle === 'Success' || modalTitle === 'Warning' || modalTitle === 'Error') {
		appendMessageModalTitleClass(modalTitle);
		return 'messageModal';
	}
	else if (modalTitle.includes("User")) {
		return 'userInformationModal';
	}
	else if (modalTitle.includes("Article")) {
		var articleContent = document.getElementById('articleContent');
		var tempEle = document.createElement('div');
		tempEle.innerHTML = modalBody;
		articleContent.innerHTML = tempEle.childNodes[0].nodeValue;
		return 'articleViewModal';
	}
	else {
		
	}
}

function showModal(modalTitle) {
	var myModal = new bootstrap.Modal(document.getElementById(selectModal(modalTitle)));
	myModal.show();
}

function appendMessageModalTitleClass(modalTitle) {
	let modalTitleObject = document.getElementById('messageModalTitle');
	if (modalTitle === 'Success') {
		modalTitleObject.classList.add('text-success');
	}
	else if (modalTitle === 'Warning') {
		modalTitleObject.classList.add('text-warning');
	}
	else if (modalTitle === 'Error') {
		modalTitleObject.classList.add('text-danger');
	}
	else {
		
	}
}

