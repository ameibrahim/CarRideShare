// app/api/faqs/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET() {
    const faqs = await prisma.fAQ.findMany();
    return NextResponse.json(faqs);
}

export async function POST(request: Request) {
    const body = await request.json();
    const { question, answer } = body;
    try {
        const faq = await prisma.fAQ.create({
            data: { question, answer },
        });
        return NextResponse.json(faq, { status: 201 });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to create FAQ" },
            { status: 500 }
        );
    }
}
