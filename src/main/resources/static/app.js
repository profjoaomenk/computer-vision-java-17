const urlInput = document.getElementById("url");
const analyzeButton = document.getElementById("analyze");
const status = document.getElementById("status");
const result = document.getElementById("result");

analyzeButton.addEventListener("click", async () => {
    const url = urlInput.value.trim();

    if (!url) {
        status.textContent = "Informe uma URL pública de imagem.";
        return;
    }

    analyzeButton.disabled = true;
    status.textContent = "Analisando imagem no Azure AI Vision...";
    result.classList.add("hidden");

    try {
        const response = await fetch("/api/analyze", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ url })
        });

        if (!response.ok) {
            const text = await response.text();
            throw new Error(text || "Erro ao analisar a imagem.");
        }

        const data = await response.json();

        document.getElementById("image").src = data.url;
        document.getElementById("caption").textContent = data.caption;
        document.getElementById("confidence").textContent =
            data.confidence == null ? "N/A" : `${(data.confidence * 100).toFixed(2)}%`;

        const tags = document.getElementById("tags");
        tags.innerHTML = "";

        data.tags.forEach(tag => {
            const span = document.createElement("span");
            span.className = "tag";
            span.textContent = tag;
            tags.appendChild(span);
        });

        result.classList.remove("hidden");
        status.textContent = "Análise concluída.";
    } catch (error) {
        status.textContent = "Erro: " + error.message;
    } finally {
        analyzeButton.disabled = false;
    }
});
