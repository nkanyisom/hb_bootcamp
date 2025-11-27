import React, { useState } from "react";
import axios from "axios";

function App() {
  const [gridX, setGridX] = useState(5);
  const [gridY, setGridY] = useState(5);
  const [rovers, setRovers] = useState([]);
  const [result, setResult] = useState([]);
  const [isDeploying, setIsDeploying] = useState(false);

  // Add a new empty rover
  const addRover = () => {
    setRovers([...rovers, { x: 0, y: 0, direction: "N", commands: "" }]);
  };

  // Update rover values
  const updateRover = (index, field, value) => {
    const newRovers = [...rovers];
    newRovers[index][field] =
      field === "x" || field === "y" ? Number(value) : value.toUpperCase();
    setRovers(newRovers);
  };

  // Delete rover
  const deleteRover = (index) => {
    const newRovers = rovers.filter((_, idx) => idx !== index);
    setRovers(newRovers);
  };

  // Send POST request
  const handleSubmit = async () => {
    if (rovers.length === 0) {
      alert("Please add at least one rover before deploying!");
      return;
    }

    // 🚨 Check if rover is out of bounds before deploying
    for (let i = 0; i < rovers.length; i++) {
      const r = rovers[i];
      if (r.x < 0 || r.x > gridX || r.y < 0 || r.y > gridY) {
        alert(
          `🚨 Rover ${i + 1} is out of grid bounds!\nAllowed: (0,0) to (${gridX},${gridY})\nFound: (${r.x},${r.y})`
        );
        return; // Stop deployment if any rover is out of bounds
      }
    }

    setIsDeploying(true);
    const payload = {
      gridX,
      gridY,
      rovers: rovers.map((r) => ({
        x: Number(r.x),
        y: Number(r.y),
        direction: r.direction,
        commands: r.commands.toUpperCase(),
      })),
    };

    console.log("Sending payload:", JSON.stringify(payload, null, 2));

    try {
      const res = await axios.post("/move", payload, {
        headers: { "Content-Type": "application/json" },
      });
      console.log("API Response:", res.data);
      await new Promise((resolve) => setTimeout(resolve, 800));
      setResult(res.data);
    } catch (error) {
      console.error("API Error:", error);
      if (error.response) {
        alert(
          `Error: ${error.response.status} - ${JSON.stringify(
            error.response.data
          )}`
        );
      } else if (error.request) {
        alert(
          "Error: No response from server. Is the backend running on http://localhost:8085?"
        );
      } else {
        alert(`Error: ${error.message}`);
      }
    } finally {
      setIsDeploying(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-b from-indigo-900 via-purple-900 to-pink-800 p-6">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-5xl font-bold mb-2 text-center text-transparent bg-clip-text bg-gradient-to-r from-yellow-400 to-red-500 animate-pulse">
          🚀 Mars Rover Mission Control 🪐
        </h1>
        <p className="text-center text-gray-300 mb-6 italic">
          Navigate the Red Planet
        </p>

        {/* Control Panel */}
        <div className="bg-gray-900 bg-opacity-80 backdrop-blur-md rounded-lg shadow-2xl p-6 mb-6 border-2 border-purple-500">
          <h2 className="text-2xl font-bold mb-4 text-yellow-400 flex items-center gap-2">
            🎮 Mission Parameters
          </h2>

          {/* Grid Size Inputs */}
          <div className="mb-6">
            <label className="text-white font-semibold mb-2 block">
              Grid Dimensions:
            </label>
            <div className="flex gap-4">
              <div className="flex items-center gap-2">
                <span className="text-gray-300">X:</span>
                <input
                  type="number"
                  value={gridX}
                  onChange={(e) => setGridX(Number(e.target.value))}
                  className="border-2 border-purple-400 bg-gray-800 text-white p-2 rounded w-20 focus:outline-none focus:ring-2 focus:ring-yellow-400"
                  placeholder="Grid X"
                />
              </div>
              <div className="flex items-center gap-2">
                <span className="text-gray-300">Y:</span>
                <input
                  type="number"
                  value={gridY}
                  onChange={(e) => setGridY(Number(e.target.value))}
                  className="border-2 border-purple-400 bg-gray-800 text-white p-2 rounded w-20 focus:outline-none focus:ring-2 focus:ring-yellow-400"
                  placeholder="Grid Y"
                />
              </div>
            </div>
          </div>

          {/* Rover Inputs */}
          <h3 className="text-xl font-semibold mb-3 text-cyan-400">
            🤖 Rover Configuration
          </h3>
          <div className="space-y-3 mb-4">
            {rovers.map((r, idx) => (
              <div
                key={idx}
                className="flex gap-2 items-center bg-gray-800 p-3 rounded-lg border border-gray-700"
              >
                <span className="text-yellow-400 font-bold mr-2">
                  R{idx + 1}
                </span>
                <input
                  type="number"
                  value={r.x}
                  onChange={(e) => updateRover(idx, "x", e.target.value)}
                  placeholder="X"
                  className="border border-gray-600 bg-gray-700 text-white p-2 rounded w-16 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                />
                <input
                  type="number"
                  value={r.y}
                  onChange={(e) => updateRover(idx, "y", e.target.value)}
                  placeholder="Y"
                  className="border border-gray-600 bg-gray-700 text-white p-2 rounded w-16 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                />
                <input
                  type="text"
                  value={r.direction}
                  onChange={(e) => updateRover(idx, "direction", e.target.value)}
                  placeholder="N/E/S/W"
                  maxLength={1}
                  className="border border-gray-600 bg-gray-700 text-white p-2 rounded w-32 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                />
                <input
                  type="text"
                  value={r.commands}
                  onChange={(e) => updateRover(idx, "commands", e.target.value)}
                  placeholder="Commands (L/R/M)"
                  className="border border-gray-600 bg-gray-700 text-white p-2 rounded flex-1 focus:outline-none focus:ring-2 focus:ring-cyan-400"
                />
                <button
                  onClick={() => deleteRover(idx)}
                  className="bg-red-500 hover:bg-red-600 text-white p-2 rounded-lg transition-all transform hover:scale-110"
                  title="Delete Rover"
                >
                  🗑️
                </button>
              </div>
            ))}
          </div>

          <div className="flex gap-4">
            <button
              className="bg-gradient-to-r from-blue-500 to-cyan-500 text-white px-6 py-3 rounded-lg font-bold hover:from-blue-600 hover:to-cyan-600 transform hover:scale-105 transition-all shadow-lg"
              onClick={addRover}
            >
              ➕ Add Rover
            </button>
            <button
              className="bg-gradient-to-r from-green-500 to-emerald-500 text-white px-6 py-3 rounded-lg font-bold hover:from-green-600 hover:to-emerald-600 transform hover:scale-105 transition-all shadow-lg disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none flex items-center gap-2"
              onClick={handleSubmit}
              disabled={isDeploying}
            >
              {isDeploying ? (
                <>
                  <svg
                    className="animate-spin h-5 w-5 text-white"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                  >
                    <circle
                      className="opacity-25"
                      cx="12"
                      cy="12"
                      r="10"
                      stroke="currentColor"
                      strokeWidth="4"
                    ></circle>
                    <path
                      className="opacity-75"
                      fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                    ></path>
                  </svg>
                  Deploying...
                </>
              ) : (
                <>🚀 Deploy Rovers</>
              )}
            </button>
          </div>
        </div>

        {/* Mars Grid */}
        <div className="bg-gray-900 bg-opacity-80 backdrop-blur-md rounded-lg shadow-2xl p-6 border-2 border-red-500">
          <h2 className="text-2xl font-bold mb-4 text-red-400 flex items-center gap-2">
            🪐 Mars Surface
          </h2>
          <div className="relative inline-block">
            {/* Y-axis labels */}
            <div
              className="absolute left-0 top-0 flex flex-col-reverse"
              style={{
                marginLeft: "-30px",
                height: `${(gridY + 1) * 60}px`,
                marginTop: "25px",
              }}
            >
              {Array.from({ length: gridY + 1 }).map((_, idx) => (
                <div
                  key={idx}
                  className="flex items-center justify-end"
                  style={{ height: "60px", width: "25px" }}
                >
                  <span className="text-sm font-bold text-cyan-400">
                    {idx}
                  </span>
                </div>
              ))}
            </div>

            {/* X-axis labels */}
            <div className="flex" style={{ marginLeft: "0px", marginBottom: "5px" }}>
              {Array.from({ length: gridX + 1 }).map((_, idx) => (
                <div
                  key={idx}
                  className="flex items-center justify-center"
                  style={{ width: "60px" }}
                >
                  <span className="text-sm font-bold text-cyan-400">{idx}</span>
                </div>
              ))}
            </div>

            {/* Grid */}
            <div
              className="relative border-4 border-orange-500 shadow-2xl"
              style={{
                width: `${(gridX + 1) * 60}px`,
                height: `${(gridY + 1) * 60}px`,
                display: "grid",
                gridTemplateColumns: `repeat(${gridX + 1}, 60px)`,
                gridTemplateRows: `repeat(${gridY + 1}, 60px)`,
                background:
                  "linear-gradient(135deg, #8B4513 0%, #CD853F 50%, #D2691E 100%)",
              }}
            >
              {Array.from({ length: (gridX + 1) * (gridY + 1) }).map((_, idx) => (
                <div
                  key={idx}
                  className="border border-orange-800 border-opacity-30"
                  style={{ width: "60px", height: "60px" }}
                />
              ))}

              {rovers.map((rover, idx) => {
                const xPos = Number(rover.x) || 0;
                const yPos = Number(rover.y) || 0;
                const dir = rover.direction || "N";

                // Check if position is valid
                if (xPos < 0 || xPos > gridX || yPos < 0 || yPos > gridY) {
                  return null;
                }

                return (
                  <div
                    key={`pre-${idx}`}
                    className="absolute bg-gradient-to-br from-blue-400 to-purple-600 text-white font-bold flex items-center justify-center rounded-full w-12 h-12 shadow-lg border-2 border-blue-200 opacity-70"
                    style={{
                      left: `${xPos * 60 + 4}px`,
                      bottom: `${yPos * 60 + 4}px`,
                    }}
                    title={`Rover ${idx + 1} (Pre-deployment): (${xPos}, ${yPos}) ${dir}`}
                  >
                    <span className="text-xs">R{idx + 1}</span>
                  </div>
                );
              })}

              {result.map((pos, idx) => {
                const [x, y, dir] = pos.split(" ");
                const xPos = Number(x);
                const yPos = Number(y);
                return (
                  <div
                    key={`deployed-${idx}`}
                    className="absolute bg-gradient-to-br from-yellow-400 to-red-600 text-white font-bold flex items-center justify-center rounded-full w-12 h-12 shadow-lg border-2 border-white animate-bounce"
                    style={{
                      left: `${xPos * 60 + 4}px`,
                      bottom: `${yPos * 60 + 4}px`,
                      animationDuration: "2s",
                    }}
                    title={`Rover ${idx + 1} (Deployed): ${pos}`}
                  >
                    {dir}
                  </div>
                );
              })}

              {isDeploying && (
                <div className="absolute inset-0 bg-black bg-opacity-70 flex items-center justify-center z-10">
                  <div className="text-center">
                    <div className="inline-block animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-yellow-400 mb-4"></div>
                    <p className="text-white text-xl font-bold animate-pulse">
                      Deploying Rovers...
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Result */}
        {result.length > 0 && (
          <div className="bg-gray-900 bg-opacity-80 backdrop-blur-md rounded-lg shadow-2xl p-6 mt-6 border-2 border-green-500">
            <h2 className="text-2xl font-bold mb-3 text-green-400 flex items-center gap-2">
              ✅ Mission Report
            </h2>
            <ul className="space-y-2">
              {result.map((pos, idx) => (
                <li
                  key={idx}
                  className="text-white bg-gray-800 p-3 rounded-lg border-l-4 border-green-500 font-mono"
                >
                  <span className="text-yellow-400 font-bold">
                    Rover {idx + 1}:
                  </span>{" "}
                  {pos}
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
