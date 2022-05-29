import '../App.css';
import React, {FC, useState} from 'react'
import {Button, ProgressBar} from "react-bootstrap";
import {fetchEventSource} from "@microsoft/fetch-event-source";

/**
 * Main page with articles list
 *
 * @returns main page
 */
export const Percents : FC = () => {

    const [percents, setPercents] = useState<number>(0);
    const [completed, setCompleted] = useState<boolean>(false);
    const [downloadingStarted, setDownloadingStarted] = useState<boolean>(false);

    function fetchElements() {
        if (completed || downloadingStarted) {
            return;
        }

        setDownloadingStarted(true);

        fetchEventSource("http://localhost:8080/article/percents", {
            method: "GET",
            headers: {
                Accept: "text/event-stream",
            },
            onmessage(event) {
                const fetchedPercents: number = JSON.parse(event.data);

                setPercents(fetchedPercents);
            },
            onclose() {
                setCompleted(true);
                setDownloadingStarted(false);
            },
            onerror(err) {
                console.log("There was an error from server", err);
                alert("There was an error from server: " + err);
            },
        });
    }

    return (
        <div className="articles-list article-width">
            <div className="progress-wrap">
                {downloadingStarted && !completed && (
                    <ProgressBar striped variant="success" now={percents} label={`${percents}%`} />
                )}

                {completed && <span>Загрузка завершена !</span> }
            </div>

            <Button variant="primary" onClick={fetchElements} disabled={completed}>Загрузить</Button>
        </div>
    );
}

