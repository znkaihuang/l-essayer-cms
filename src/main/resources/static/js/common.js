/**
 * 
 */

function selectModal(modalTitle) {
	if (modalTitle !== 'Success' && modalTitle !== 'Warning' && modalTitle !== 'Error') {
		return 'userInformationModal';
	}
	else {
		return 'messageModal';
	}
}

function showModal(modalTitle) {
	var myModal = new bootstrap.Modal(document.getElementById(selectModal(modalTitle)));
	appendMessageModalTitleClass(modalTitle);
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

