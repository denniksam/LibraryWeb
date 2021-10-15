document.addEventListener("DOMContentLoaded",()=>{
    const container =
        document.getElementById("books-container")
    if(!container) throw "books-container not found";
    // fetch data (books)
    fetch("books")
        .then(r => r.json())
        .then(j => fillContainer(container, j));
});

function fillContainer(container, j) {
    // fetch template (html)
    fetch("templates/bookitem.html")
        .then(r => r.text())
        .then( tpl => {
            var html = "" ;
            for(let book of j){
                html += tpl
                    .replace("{{author}}", book["author"])
                    .replace("{{title}}",  book["title"])
                    .replace("{{cover}}",  book["cover"]);
            }
            container.innerHTML = html;
        });

}