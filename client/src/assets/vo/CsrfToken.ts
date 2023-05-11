export class CsrfToken {
    private _token: string;

    private _parameterName: string;

    private _headerName: string;


    constructor(token: string, parameterName: string, headerName: string) {
        this._token = token;
        this._parameterName = parameterName;
        this._headerName = headerName;
    }

    public get token(): string {
        return this._token;
    }

    public set token(value: string) {
        this._token = value;
    }

    public get parameterName(): string {
        return this._parameterName;
    }

    public set parameterName(value: string) {
        this._parameterName = value;
    }

    public get headerName(): string {
        return this._headerName;
    }

    public set headerName(value: string) {
        this._headerName = value;
    }
}