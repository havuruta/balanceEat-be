<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../layout/header.jsp" %>

<div class="row mb-4">
    <div class="col">
        <h2>영양 정보 목록</h2>
    </div>
</div>

<div class="row mb-4">
    <h4>카테고리로 검색</h4>
    <div class="col">
        <form action="/nutrition/search" method="get" class="d-flex">
            <input type="text" name="description" class="form-control me-4" value="${searchDescription}">
            <button type="submit" class="btn btn-primary">검색</button>
        </form>
    </div>

    <h4>식품명 검색</h4>
    <div>
        <form action="/nutrition/search" method="get" class="d-flex">
            <input type="text" name="name" class="form-control me-4" value="${searchByName}">
            <button type="submit" class="btn btn-primary">검색</button>
        </form>
    </div>
</div>

<div class="table-responsive">
    <table class="table table-striped table-hover">
        <thead>
            <tr>
                <th>ID</th>
                <th>식품명</th>
                <th>칼로리 (kcal)</th>
                <th>단백질 (g)</th>
                <th>지방 (g)</th>
                <th>탄수화물 (g)</th>
                <th>카테고리</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${nutritions}" var="nutrition">
                <tr>
                    <td>${nutrition.id}</td>
                    <td>${nutrition.name}</td>
                    <td>${nutrition.calories}</td>
                    <td>${nutrition.protein}</td>
                    <td>${nutrition.fat}</td>
                    <td>${nutrition.carbohydrates}</td>
                    <td>${nutrition.description}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %> 