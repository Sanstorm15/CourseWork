import React, { useState } from 'react'

export default function JournalForm({ onAdd, loading }) {
  const [name, setName] = useState('')
  const [subject, setSubject] = useState('')
  const [grade, setGrade] = useState('')
  const [errors, setErrors] = useState({})

  const subjects = [
    'Математика', 'Українська мова', 'Англійська мова', 'Історія', 
    'Географія', 'Біологія', 'Фізика', 'Хімія', 'Фізкультура', 
    'Інформатика', 'Література', 'Мистецтво'
  ]

  const validateForm = () => {
    const newErrors = {}
    
    if (!name.trim()) newErrors.name = "Ім'я обов'язкове"
    if (!subject) newErrors.subject = "Виберіть предмет"
    if (!grade) newErrors.grade = "Оцінка обов'язкова"
    else if (grade < 1 || grade > 12) newErrors.grade = "Оцінка має бути від 1 до 12"
    
    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    
    if (!validateForm()) return
    
    onAdd({ 
      name: name.trim(), 
      subject, 
      grade: parseInt(grade) 
    })
    
    setName('')
    setSubject('')
    setGrade('')
    setErrors({})
  }

  const getGradeColor = (gradeValue) => {
    if (gradeValue >= 10) return '#10b981' // зелений
    if (gradeValue >= 7) return '#f59e0b'  // жовтий
    return '#ef4444' // червоний
  }

  return (
    <form onSubmit={handleSubmit} className="journal-form">
      <div className="form-group">
        <label htmlFor="name">👤 Ім'я учня</label>
        <input 
          id="name"
          type="text"
          value={name} 
          onChange={(e) => setName(e.target.value)}
          placeholder="Введіть ім'я учня..."
          className={errors.name ? 'error' : ''}
        />
        {errors.name && <span className="error-message">{errors.name}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="subject">📖 Предмет</label>
        <select 
          id="subject"
          value={subject} 
          onChange={(e) => setSubject(e.target.value)}
          className={errors.subject ? 'error' : ''}
        >
          <option value="">Виберіть предмет...</option>
          {subjects.map(subj => (
            <option key={subj} value={subj}>{subj}</option>
          ))}
        </select>
        {errors.subject && <span className="error-message">{errors.subject}</span>}
      </div>

      <div className="form-group">
        <label htmlFor="grade">⭐ Оцінка (1-12)</label>
        <div className="grade-input-container">
          <input 
            id="grade"
            type="number" 
            value={grade} 
            onChange={(e) => setGrade(e.target.value)}
            min="1" 
            max="12"
            placeholder="Оцінка"
            className={errors.grade ? 'error' : ''}
          />
          {grade && (
            <div 
              className="grade-preview"
              style={{ backgroundColor: getGradeColor(parseInt(grade)) }}
            >
              {grade}
            </div>
          )}
        </div>
        {errors.grade && <span className="error-message">{errors.grade}</span>}
      </div>

      <button 
        type="submit" 
        className="submit-btn"
        disabled={loading}
      >
        {loading ? (
          <>
            <div className="spinner"></div>
            Додавання...
          </>
        ) : (
          <>
            ➕ Додати запис
          </>
        )}
      </button>
    </form>
  )
}