import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './BowlingGame.css';

function BowlingGame() {
  const [players] = useState(['Zethu', 'Octavia']);
  const [selectedPlayer, setSelectedPlayer] = useState('Zethu');
  const [pins, setPins] = useState('');
  const [score, setScore] = useState(0);
  const [rollHistory, setRollHistory] = useState([]);
  const [frames, setFrames] = useState([]);

  const backendUrl = 'http://localhost:8080/api';

  const fetchScore = async () => {
    try {
      const response = await axios.get(`${backendUrl}/score/${selectedPlayer}`);
      setScore(response.data.score);
    } catch (error) {
      console.error('Error fetching score:', error);
    }
  };

  const fetchFrames = async () => {
    try {
      const response = await axios.get(`${backendUrl}/frames/${selectedPlayer}`);
      setFrames(response.data);
    } catch (error) {
      console.error('Error fetching frames:', error);
    }
  };

  const handleRoll = async () => {
    const pinsInt = parseInt(pins);
    if (isNaN(pinsInt) || pinsInt < 0 || pinsInt > 10) {
      alert('Enter a number between 0 and 10');
      return;
    }

    try {
      await axios.post(`${backendUrl}/roll/${selectedPlayer}`, { pins: pinsInt });
      setRollHistory([...rollHistory, pinsInt]);
      setPins('');
      fetchScore();
      fetchFrames();
    } catch (error) {
      alert(error.response?.data || 'Invalid roll: total pins in a frame cannot exceed 10');
    }
  };

  const startNewGame = async () => {
    try {
      await axios.post(`${backendUrl}/new-game/${selectedPlayer}`);
      setRollHistory([]);
      setScore(0);
      setFrames([]);
    } catch (error) {
      console.error('Error starting new game:', error);
    }
  };

  useEffect(() => {
    fetchScore();
    fetchFrames();
  }, [selectedPlayer]);

  const formatRoll = (roll, index, frameRolls) => {
    if (roll === 'X') return 'X';
    if (roll === '/' || (index === 1 && parseInt(frameRolls[0]) + parseInt(roll) === 10)) return '/';
    return roll;
  };

  return (
    <div className="container">
      <h2>Bowling Game Scorer</h2>

      <label>Select Player: </label>
      <select value={selectedPlayer} onChange={(e) => setSelectedPlayer(e.target.value)}>
        {players.map((p, i) => (
          <option key={i} value={p}>{p}</option>
        ))}
      </select>

      <br /><br />
      <input
        type="number"
        value={pins}
        onChange={(e) => setPins(e.target.value)}
        placeholder="Pins knocked down"
      />
      <button onClick={handleRoll}>Roll</button>
      <button onClick={startNewGame} style={{ marginLeft: '10px' }}>New Game</button>

      <h3>Score: {score}</h3>
      <h4>Roll History:</h4>
      <ul>
        {rollHistory.map((r, i) => (
          <li key={i}>Roll {i + 1}: {r}</li>
        ))}
      </ul>

      <h4>Scorecard:</h4>
      <table className="scorecard">
        <thead>
          <tr>
            {[...Array(10)].map((_, i) => <th key={i}>Frame {i + 1}</th>)}
          </tr>
        </thead>
        <tbody>
          <tr>
            {frames.map((frame, i) => {
              const frameType = frame.rolls.includes('X')
                ? 'strike'
                : frame.rolls.includes('/')
                  ? 'spare'
                  : 'open';

              return (
                <td key={i} className={`frame-cell ${frameType}`}>
                  {frame.rolls.map((r, j) => (
                    <span key={j} className={r === 'X' ? 'strike' : r === '/' ? 'spare' : ''}>
                      {formatRoll(r, j, frame.rolls)}{' '}
                    </span>
                  ))}
                </td>
              );
            })}
          </tr>
          <tr>
            {frames.map((frame, i) => (
              <td key={i}>{frame.score}</td>
            ))}
          </tr>
        </tbody>
      </table>
    </div>
  );
}

export default BowlingGame;