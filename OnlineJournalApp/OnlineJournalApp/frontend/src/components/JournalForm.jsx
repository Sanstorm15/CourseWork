
import React, { useState } from 'react'

export default function JournalForm({ onAdd }) {
  const [name, setName] = useState('')
  const [subject, setSubject] = useState('')
  const [grade, setGrade] = useState('')

  const handleSubmit = (e) => {
    e.preventDefault()
    onAdd({ name, subject, grade })
    setName('')
    setSubject('')
    setGrade('')
  }

  return (
    <form onSubmit={handleSubmit}>
      <label>Ім'я учня:</label>
      <input value={name} onChange={(e) => setName(e.target.value)} required /><br /><br />
      <label>Предмет:</label>
      <input value={subject} onChange={(e) => setSubject(e.target.value)} required /><br /><br />
      <label>Оцінка:</label>
      <input type="number" value={grade} onChange={(e) => setGrade(e.target.value)} required min="1" max="12" /><br /><br />
      <button type="submit">Додати запис</button>
    </form>
  )
}
