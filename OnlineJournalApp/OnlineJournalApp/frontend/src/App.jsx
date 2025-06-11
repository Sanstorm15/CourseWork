
import React, { useState } from 'react'
import JournalForm from './components/JournalForm'
import JournalTable from './components/JournalTable'

export default function App() {
  const [entries, setEntries] = useState([])

  const addEntry = async (entry) => {
    const res = await fetch('/api/grades', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(entry)
    })
    if (res.ok) {
      setEntries([...entries, entry])
    } else {
      alert("Помилка при збереженні")
    }
  }

  const deleteEntry = (index) => {
    const newEntries = entries.filter((_, i) => i !== index)
    setEntries(newEntries)
  }

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial' }}>
      <h1>Онлайн-щоденник</h1>
      <JournalForm onAdd={addEntry} />
      <JournalTable entries={entries} onDelete={deleteEntry} />
    </div>
  )
}
