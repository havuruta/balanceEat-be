<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>

<div class="row mb-4">
    <div class="col">
        <h2>영양 정보 수정</h2>
    </div>
</div>

<div class="card">
    <div class="card-body">
        <form id="nutritionForm" onsubmit="return submitForm(event)">
            <input type="hidden" id="id" name="id" value="${nutrition.id}">
            <div class="mb-3">
                <label for="foodName" class="form-label">식품명</label>
                <input type="text" class="form-control" id="foodName" name="foodName" value="${nutrition.foodName}" required>
            </div>
            <div class="mb-3">
                <label for="calories" class="form-label">칼로리 (kcal)</label>
                <input type="number" class="form-control" id="calories" name="calories" value="${nutrition.calories}" required>
            </div>
            <div class="mb-3">
                <label for="protein" class="form-label">단백질 (g)</label>
                <input type="number" class="form-control" id="protein" name="protein" value="${nutrition.protein}" required>
            </div>
            <div class="mb-3">
                <label for="fat" class="form-label">지방 (g)</label>
                <input type="number" class="form-control" id="fat" name="fat" value="${nutrition.fat}" required>
            </div>
            <div class="mb-3">
                <label for="carbohydrate" class="form-label">탄수화물 (g)</label>
                <input type="number" class="form-control" id="carbohydrate" name="carbohydrate" value="${nutrition.carbohydrate}" required>
            </div>
            <div class="text-end">
                <a href="/nutrition/list" class="btn btn-secondary">취소</a>
                <button type="submit" class="btn btn-primary">저장</button>
            </div>
        </form>
    </div>
</div>

<script>
function submitForm(event) {
    event.preventDefault();
    
    const formData = {
        id: document.getElementById('id').value,
        foodName: document.getElementById('foodName').value,
        calories: document.getElementById('calories').value,
        protein: document.getElementById('protein').value,
        fat: document.getElementById('fat').value,
        carbohydrate: document.getElementById('carbohydrate').value
    };

    fetch('/nutrition/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/nutrition/list';
        } else {
            alert('수정 중 오류가 발생했습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('수정 중 오류가 발생했습니다.');
    });

    return false;
}
</script>

<%@ include file="../layout/footer.jsp" %> 