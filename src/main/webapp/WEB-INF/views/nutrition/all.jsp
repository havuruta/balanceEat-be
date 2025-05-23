<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../layout/header.jsp" %>

<div class="container">
    <h2 class="mb-4">모든 영양 정보</h2>
    
    <div class="row mb-4">
        <div class="col">
            <form action="/nutrition/search" method="get" class="d-flex">
                <input type="text" name="description" class="form-control me-2" placeholder="설명으로 검색" value="${searchDescription}">
                <button type="submit" class="btn btn-primary">검색</button>
            </form>
        </div>
    </div>
    
    <div class="table-responsive">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>식품명</th>
                    <th>칼로리</th>
                    <th>단백질</th>
                    <th>지방</th>
                    <th>탄수화물</th>
                </tr>
            </thead>
            <tbody id="nutritionList">
                <!-- 데이터는 JavaScript로 동적 로딩됩니다 -->
            </tbody>
        </table>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // URL에서 검색 파라미터 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    const searchDescription = urlParams.get('description');
    
    // 검색어가 있으면 검색 API 호출, 없으면 전체 목록 API 호출
    const url = searchDescription ? `/nutrition/search?description=${encodeURIComponent(searchDescription)}` : '/nutrition/all';
    
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById('nutritionList');
            tbody.innerHTML = '';
            
            data.forEach(nutrition => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${nutrition.id}</td>
                    <td>${nutrition.description}</td>
                    <td>${nutrition.calories}</td>
                    <td>${nutrition.protein}</td>
                    <td>${nutrition.fat}</td>
                    <td>${nutrition.carbohydrate}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            alert('데이터를 불러오는 중 오류가 발생했습니다.');
        });
});
</script>

<%@ include file="../layout/footer.jsp" %> 