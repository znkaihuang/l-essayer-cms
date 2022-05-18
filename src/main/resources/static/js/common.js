/**
 * 
 */

function selectModal() {
	if (modalTitle !== 'Success' && modalTitle !== 'Warning' && modalTitle !== 'Error') {
		return 'userInformationModal';
	}
	else {
		return 'messageModal';
	}
}

function showModal() {
	var myModal = new bootstrap.Modal(document.getElementById(selectModal()));
	appendMessageModalTitleClass();
	myModal.show();
}

function appendMessageModalTitleClass() {
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

