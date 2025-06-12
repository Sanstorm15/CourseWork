import React, { useState, useEffect } from 'react'
import JournalForm from './components/JournalForm'
import JournalTable from './components/JournalTable'
import "./App.css"

export default function App() {
  const [entries, setEntries] = useState([])
  const [students, setStudents] = useState([])
  const [loading, setLoading] = useState(false)

  // Завантаження даних при запуску
  useEffect(() => {
    loadGrades()
    loadStudents()
  }, [])

  const loadGrades = async () => {
    try {
      const response = await fetch('/api/grades')
      if (response.ok) {
        const grades = await response.json()
        setEntries(grades)
      }
    } catch (error) {
      console.error('Помилка завантаження оцінок:', error)
    }
  }

  const loadStudents = async () => {
    try {
      const response = await fetch('/api/students')
      if (response.ok) {
        const studentsData = await response.json()
        setStudents(studentsData)
      }
    } catch (error) {
      console.error('Помилка завантаження студентів:', error)
    }
  }

  const addEntry = async (entry) => {
    setLoading(true)
    try {
      // Спочатку створюємо або знаходимо студента
      let student = students.find(s => s.name === entry.name)
      
      if (!student) {
        const studentResponse = await fetch('/api/students', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ name: entry.name })
        })
        
        if (studentResponse.ok) {
          student = await studentResponse.json()
          setStudents([...students, student])
        }
      }

      // Тепер додаємо оцінку
      const gradeResponse = await fetch(`/api/grades/${student.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          subject: entry.subject,
          grade: parseInt(entry.grade)
        })
      })

      if (gradeResponse.ok) {
        const newGrade = await gradeResponse.json()
        setEntries([...entries, newGrade])
      } else {
        throw new Error('Помилка при додаванні оцінки')
      }
    } catch (error) {
      alert("Помилка при збереженні: " + error.message)
    } finally {
      setLoading(false)
    }
  }

  const deleteEntry = async (gradeId) => {
    try {
      const response = await fetch(`/api/grades/${gradeId}`, {
        method: 'DELETE'
      })
      
      if (response.ok) {
        setEntries(entries.filter(entry => entry.id !== gradeId))
      } else {
        throw new Error('Помилка при видаленні')
      }
    } catch (error) {
      alert("Помилка при видаленні: " + error.message)
    }
  }

  const calculateAverage = () => {
    if (entries.length === 0) return 0
    const sum = entries.reduce((acc, entry) => acc + entry.grade, 0)
    return (sum / entries.length).toFixed(1)
  }

  return (
    <div className="app">
      <div className="container">
        <header className="header">
          <h1>📚 Онлайн-щоденник учня</h1>
          <div className="stats">
            <div className="stat-card">
              <span className="stat-number">{entries.length}</span>
              <span className="stat-label">Всього оцінок</span>
            </div>
            <div className="stat-card">
              <span className="stat-number">{students.length}</span>
              <span className="stat-label">Студентів</span>
            </div>
            <div className="stat-card">
              <span className="stat-number">{calculateAverage()}</span>
              <span className="stat-label">Середній бал</span>
            </div>
          </div>
        </header>

        <div className="content">
          <div className="form-section">
            <h2>Додати нову оцінку</h2>
            <JournalForm onAdd={addEntry} loading={loading} />
          </div>

          <div className="table-section">
            <h2>Список оцінок</h2>
            <JournalTable entries={entries} onDelete={deleteEntry} />
          </div>
        </div>
      </div>
    </div>
  )
}