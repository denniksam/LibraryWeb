document.addEventListener("submit",sendClick);

function sendClick(e) {
    e.preventDefault();
    // console.log(e.target); return;  // e.target - form
    const author = e.target.querySelector("input[name=author]");
    const title  = e.target.querySelector("input[name=title]");
    const cover  = e.target.querySelector("input[name=cover]");
    // TODO data validation


    const formData = new FormData();
    formData.append("author", author.value);
    formData.append("title",  title.value);
    formData.append("cover",  cover.files[0]);
    fetch("books", { method: "POST", body: formData })
        .then(r => r.text()).then(console.log)
}