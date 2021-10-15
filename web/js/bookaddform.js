document.addEventListener("submit",sendClick);
document.addEventListener("DOMContentLoaded", ()=>{
    const cover = document.getElementById("cover");
    cover.onchange = coverChange;
});
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

function coverChange(e) {
    if(e.target.files) {
        const coverImg = document.getElementById("coverImg");
        coverImg.src=URL.createObjectURL(e.target.files[0]);
    }
}

