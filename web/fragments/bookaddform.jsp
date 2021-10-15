<%@ page contentType="text/html;charset=UTF-8" %>
<div class="book-add-form">
    <form method="post" enctype="multipart/form-data">
        <input id="cover" name="cover" type="file">
        <label for="cover"><img src="" id="coverImg"/></label>
        <label>Author <input name="author"></label>
        <br/>
        <label>Title <input name="title"></label>
        <br/>
        <input type="submit" value="Add"/>
    </form>
</div>
<script src="js/bookaddform.js"></script>
