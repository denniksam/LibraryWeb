<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="book-add-form">
    <form method="post" enctype="multipart/form-data">
        <label>Author <input name="author"></label>
        <br/>
        <label>Title <input name="title"></label>
        <br/>
        <label>Cover <input name="cover" type="file"></label>
        <br/>
        <input type="submit" value="Add"/>
    </form>
</div>
<script src="js/bookaddform.js"></script>
