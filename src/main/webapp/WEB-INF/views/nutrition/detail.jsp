<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>

<div class="row mb-4">
    <div class="col">
        <h2>영양 정보 상세</h2>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">ID</div>
            <div class="col-md-9">${nutrition.id}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">식품명</div>
            <div class="col-md-9">${nutrition.name}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">칼로리 (kcal)</div>
            <div class="col-md-9">${nutrition.calories}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">단백질 (g)</div>
            <div class="col-md-9">${nutrition.protein}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">지방 (g)</div>
            <div class="col-md-9">${nutrition.fat}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">탄수화물 (g)</div>
            <div class="col-md-9">${nutrition.carbohydrates}</div>
        </div>
        <div class="row mb-3">
            <div class="col-md-3 fw-bold">설명</div>
            <div class="col-md-9">${nutrition.description}</div>
        </div>
        <div class="text-end">
            <a href="/nutrition/list" class="btn btn-secondary">목록으로</a>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %> 