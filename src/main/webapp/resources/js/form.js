function addAuthorSelect() {
    var elmnt = document.getElementById("authorSelect");
    var cln = elmnt.cloneNode(true);
    document.getElementById("authorSelects").appendChild(cln);
}
function addNewAuthorRow() {
    var elmnt = document.getElementById("newAuthor");
    var cln = elmnt.cloneNode(true);
    document.getElementById("newAuthorsTable").appendChild(cln);
}
function addKeywordSelect() {
    var elmnt = document.getElementById("keywordSelect");
    var cln = elmnt.cloneNode(true);
    document.getElementById("keywordSelects").appendChild(cln);
}
function addNewKeywordRow() {
    var elmnt = document.getElementById("newKeyword");
    var cln = elmnt.cloneNode(true);
    document.getElementById("newKeywordsTable").appendChild(cln);
}
// function deleteTagById(id){
//     document.getElementById(id).remove();
// }

function deleteNodeById(id){
    var elementForDeleting = document.getElementById(id);
    elementForDeleting.parentElement.removeChild(elementForDeleting);
}
