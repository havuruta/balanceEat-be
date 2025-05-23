<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 정보 수정</title>
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
        }
        
        .form-group {
            margin-bottom: 15px;
        }
        
        label {
            display: block;
            margin-bottom: 5px;
        }
        
        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        
        .btn-group {
            margin-top: 20px;
            display: flex;
            gap: 1rem;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            text-align: center;
        }
        
        .btn-submit {
            background-color: #4CAF50;
            color: white;
        }
        
        .btn-cancel {
            background-color: #f44336;
            color: white;
        }
    </style>
</head>
<body>
    <%@ include file="/WEB-INF/views/layout/header.jsp" %>
    
    <div class="form-container">
        <h1>회원 정보 수정</h1>
        <form action="/user/update" method="post">
            <input type="hidden" name="id" value="${user.id}">
            
            <div class="form-group">
                <label for="username">아이디</label>
                <input type="text" id="username" name="username" value="${user.username}" readonly>
            </div>
            
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn btn-submit">수정</button>
                <a href="/user/mypage" class="btn btn-cancel">취소</a>
            </div>
        </form>
    </div>
</body>
</html> 