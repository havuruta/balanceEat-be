<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header">
                    <h3 class="text-center">회원가입</h3>
                </div>
                <div class="card-body">
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger" role="alert">
                            <strong>오류:</strong> <%= request.getAttribute("error") %>
                            <% if (request.getAttribute("errorDetail") != null) { %>
                                <hr>
                                <small class="text-muted">
                                    <%= request.getAttribute("errorDetail") %>
                                </small>
                            <% } %>
                        </div>
                    <% } %>
                    <form action="/auth/register" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">아이디</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">비밀번호</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <div class="mb-3">
                            <label for="passwordConfirm" class="form-label">비밀번호 확인</label>
                            <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" required>
                        </div>
                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary">회원가입</button>
                            <a href="/auth/login" class="btn btn-secondary">로그인으로 돌아가기</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %> 