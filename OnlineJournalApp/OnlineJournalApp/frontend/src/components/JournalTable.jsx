
import React from 'react'

export default function JournalTable({ entries, onDelete }) {
  return (
    <table border="1" cellPadding="10" style={{ marginTop: '20px', width: '100%' }}>
      <thead>
        <tr>
          <th>Ім'я учня</th>
          <th>Предмет</th>
          <th>Оцінка</th>
          <th>Дія</th>
        </tr>
      </thead>
      <tbody>
        {entries.map((entry, index) => (
          <tr key={index}>
            <td>{entry.name}</td>
            <td>{entry.subject}</td>
            <td>{entry.grade}</td>
            <td><button onClick={() => onDelete(index)}>Видалити</button></td>
          </tr>
        ))}
      </tbody>
    </table>
  )
}
