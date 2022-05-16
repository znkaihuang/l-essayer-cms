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
	myModal.show();
}