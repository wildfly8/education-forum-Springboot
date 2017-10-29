function getSummernoteCode() {
	var codeValue = $('#summernote').summernote('code');
	document.getElementById('summernoteContent').value = codeValue;
}