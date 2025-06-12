import React, { useState } from 'react'

export default function JournalTable({ entries, onDelete }) {
  const [sortField, setSortField] = useState('id')
  const [sortDirection, setSortDirection] = useState('desc')
  const [filterSubject, setFilterSubject] = useState('')
  const [searchStudent, setSearchStudent] = useState('')

  const handleSort = (field) => {
    if (sortField === field) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc')
    } else {
      setSortField(field)
      setSortDirection('asc')
    }
  }

  const getGradeBadgeClass = (grade) => {
    if (grade >= 10) return 'grade-excellent'
    if (grade >= 7) return 'grade-good'
    if (grade >= 4) return 'grade-satisfactory'
    return 'grade-poor'
  }

  const getGradeEmoji = (grade) => {
    if (grade >= 10) return '🏆'
    if (grade >= 7) return '👍'
    if (grade >= 4) return '👌'
    return '😞'
  }

  const filteredAndSortedEntries = entries
    .filter(entry => {
      const matchesSubject = !filterSubject || entry.subject === filterSubject
      const matchesStudent = !searchStudent || 
        (entry.student && entry.student.name.toLowerCase().includes(searchStudent.toLowerCase()))
      return matchesSubject && matchesStudent
    })
    .sort((a, b) => {
      let aValue, bValue
      
      switch (sortField) {
        case 'student':
          aValue = a.student?.name || ''
          bValue = b.student?.name || ''
          break
        case 'subject':
          aValue = a.subject
          bValue = b.subject
          break
        case 'grade':
          aValue = a.grade
          bValue = b.grade
          break
        default:
          aValue = a.id
          bValue = b.id
      }
      
      if (sortDirection === 'asc') {
        return aValue > bValue ? 1 : -1
      } else {
        return aValue < bValue ? 1 : -1
      }
    })

  const uniqueSubjects = [...new Set(entries.map(entry => entry.subject))].sort()

  const calculateStats = () => {
    const filtered = filteredAndSortedEntries
    if (filtered.length === 0) return { avg: 0, max: 0, min: 0 }
    
    const grades = filtered.map(e => e.grade)
    return {
      avg: (grades.reduce((a, b) => a + b, 0) / grades.length).toFixed(1),
      max: Math.max(...grades),
      min: Math.min(...grades)
    }
  }

  const stats = calculateStats()

  if (entries.length === 0) {
    return (
      <div className="empty-state">
        <div className="empty-icon">📝</div>
        <h3>Поки що немає оцінок</h3>
        <p>Додайте першу оцінку, використовуючи форму вище</p>
      </div>
    )
  }

  return (
    <div className="table-container">
      <div className="table-controls">
        <div className="filters">
          <input
            type="text"
            placeholder="🔍 Пошук за ім'ям учня..."
            value={searchStudent}
            onChange={(e) => setSearchStudent(e.target.value)}
            className="search-input"
          />
          
          <select
            value={filterSubject}
            onChange={(e) => setFilterSubject(e.target.value)}
            className="filter-select"
          >
            <option value="">📚 Всі предмети</option>
            {uniqueSubjects.map(subject => (
              <option key={subject} value={subject}>{subject}</option>
            ))}
          </select>
        </div>

        <div className="table-stats">
          <span>Показано: {filteredAndSortedEntries.length} з {entries.length}</span>
          {filteredAndSortedEntries.length > 0 && (
            <span>| Середній: {stats.avg} | Макс: {stats.max} | Мін: {stats.min}</span>
          )}
        </div>
      </div>

      <div className="table-wrapper">
        <table className="journal-table">
          <thead>
            <tr>
              <th 
                onClick={() => handleSort('student')}
                className={sortField === 'student' ? 'sortable active' : 'sortable'}
              >
                👤 Ім'я учня
                {sortField === 'student' && (
                  <span className="sort-indicator">
                    {sortDirection === 'asc' ? '↑' : '↓'}
                  </span>
                )}
              </th>
              <th 
                onClick={() => handleSort('subject')}
                className={sortField === 'subject' ? 'sortable active' : 'sortable'}
              >
                📖 Предмет
                {sortField === 'subject' && (
                  <span className="sort-indicator">
                    {sortDirection === 'asc' ? '↑' : '↓'}
                  </span>
                )}
              </th>
              <th 
                onClick={() => handleSort('grade')}
                className={sortField === 'grade' ? 'sortable active' : 'sortable'}
              >
                ⭐ Оцінка
                {sortField === 'grade' && (
                  <span className="sort-indicator">
                    {sortDirection === 'asc' ? '↑' : '↓'}
                  </span>
                )}
              </th>
              <th>🔧 Дії</th>
            </tr>
          </thead>
          <tbody>
            {filteredAndSortedEntries.map((entry) => (
              <tr key={entry.id} className="table-row">
                <td className="student-cell">
                  <div className="student-info">
                    <span className="student-name">
                      {entry.student?.name || 'Невідомий студент'}
                    </span>
                  </div>
                </td>
                <td className="subject-cell">
                  <span className="subject-tag">{entry.subject}</span>
                </td>
                <td className="grade-cell">
                  <div className={`grade-badge ${getGradeBadgeClass(entry.grade)}`}>
                    <span className="grade-emoji">{getGradeEmoji(entry.grade)}</span>
                    <span className="grade-value">{entry.grade}</span>
                  </div>
                </td>
                <td className="actions-cell">
                  <button 
                    onClick={() => onDelete(entry.id)}
                    className="delete-btn"
                    title="Видалити оцінку"
                  >
                    🗑️
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}