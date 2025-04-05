// app/api/faqs/[id]/route.ts
import { NextResponse } from "next/server";
import { PrismaClient } from "@prisma/client";

const prisma = new PrismaClient();

export async function GET(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;
    const faq = await prisma.fAQ.findUnique({ where: { id } });
    if (!faq) {
        return NextResponse.json({ error: "FAQ not found" }, { status: 404 });
    }
    return NextResponse.json(faq);
}

export async function PUT(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;
    const body = await request.json();
    try {
        const updatedFaq = await prisma.fAQ.update({
            where: { id },
            data: body,
        });
        return NextResponse.json(updatedFaq);
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to update FAQ" },
            { status: 500 }
        );
    }
}

export async function DELETE(
    request: Request,
    { params }: { params: Promise<{ id: string }> }
) {
    const { id } = await params;
    try {
        await prisma.fAQ.delete({ where: { id } });
        return NextResponse.json({ message: "FAQ deleted" });
    } catch (error) {
        console.log("error: ", error);

        return NextResponse.json(
            { error: "Failed to delete FAQ" },
            { status: 500 }
        );
    }
}
