/**
 * 
 */
 
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
