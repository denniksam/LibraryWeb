document.addEventListener("submit",sendClick);

function sendClick(e) {
    e.preventDefault();

    // console.log(e.target); return;  // e.target - form

    const formData = new FormData();
    formData.append("author", "");
    fetch("/books", {
        method: "POST",
        body: formData
    }).then(r => r.text()).then(console.log)
}